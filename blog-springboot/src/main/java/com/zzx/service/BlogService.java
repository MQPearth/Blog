package com.zzx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzx.config.ImgUploadConfig;
import com.zzx.config.RabbitMqConfig;
import com.zzx.config.RedisConfig;
import com.zzx.dao.BlogDao;
import com.zzx.dao.RoleDao;
import com.zzx.dao.TagDao;
import com.zzx.dao.UserDao;
import com.zzx.model.pojo.Blog;
import com.zzx.model.pojo.User;
import com.zzx.schedule.BlogTask;
import com.zzx.utils.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FormatUtil formatUtil;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private UUIDUtil uuidUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private BlogTask blogTask;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ImgUploadConfig imgUploadConfig;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 返回的首页博客列表内容的最大字符数
     */
    private static final int MAX_BODY_CHAR_COUNT = 150;


    /**
     * 保存图片,返回url
     *
     * @param file
     * @return
     * @throws IOException
     */
    public synchronized String saveImg(MultipartFile file) throws IOException {

        //获取图片格式/后缀
        String format = formatUtil.getFileFormat(file.getOriginalFilename());
        //获取图片保存路径
        String savePath = fileUtil.getSavePath();
        //存储已满
        if (!formatUtil.checkStringNull(savePath)) {
            throw new IOException("存储已满 请联系管理员");
        }
        //保存图片
        String fileName = uuidUtil.generateUUID() + format;
        File diskFile = new File(savePath + "/" + fileName);
        file.transferTo(diskFile);
        //将硬盘路径转换为url，返回
        return imgUploadConfig.getStaticAccessPath().replaceAll("\\*", "") + fileName;
    }

    /**
     * 保存博文
     *
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));

        for (Integer tagId : tagIds) {
            // 通过标签id检查标签是否属于该用户
            if (!tagDao.findTagById(tagId).getUser().getId().equals(user.getId())) {
                throw new RuntimeException();
            }
        }


        Blog blog = new Blog();
        //博文用户
        blog.setUser(user);
        //浏览量
        blog.setBlogViews(0);
        //评论数
        blog.setDiscussCount(0);
        //标题
        blog.setTitle(blogTitle);
        //内容
        blog.setBody(blogBody);
        //1 正常状态
        blog.setState(1);
        //点赞数
        blog.setLikeCount(0);
        //发布时间
        blog.setTime(dateUtil.getCurrentDate());
        blogDao.saveBlog(blog);

        for (Integer tagId : tagIds) {
            //保存该博文的标签
            blogDao.saveBlogTag(blog.getId(), tagId);
        }

        //删除博客归档的缓存
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);

        //移除 最后一位
        if (redisTemplate.opsForList().size(RedisConfig.REDIS_NEW_BLOG) >= RedisConfig.REDIS_NEW_BLOG_COUNT) {
            redisTemplate.opsForList().rightPop(RedisConfig.REDIS_NEW_BLOG);
        }


        // 获取标签名
        blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        // 存入newblog 的左边第一位
        redisTemplate.opsForList().leftPush(RedisConfig.REDIS_NEW_BLOG, blog.getId().toString());
        // user隐藏相关字段
        blog.getUser().setPassword(null);
        blog.getUser().setMail(null);
        blog.getUser().setState(null);
        redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blog.getId(), objectMapper.writeValueAsString(blog));
    }

    /**
     * 根据id查询博文以及博文标签
     * 正常状态
     *
     * @param blogId
     * @return
     */
    public Blog findBlogById(Integer blogId, boolean isHistory) throws IOException {

        // 查询缓存
        String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
        Blog blog = null;
        if (null != blogJson) {
            // 有缓存
            blog = objectMapper.readValue(blogJson, Blog.class);
        } else {
            blog = blogDao.findBlogById(blogId);
            if (blog == null) {
                throw new RuntimeException("博客不存在");
            }
            blog.setTags(tagDao.findTagByBlogId(blogId));
        }

        //历史查看过
        if (isHistory) {
            // 直接返回 浏览量不增加
            return blog;
        } else {
            // 浏览量 + 1
            blog.setBlogViews(blog.getBlogViews() + 1);

            if (null != blogJson) {
                // 有缓存 同步操作redis
                redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
                //异步操作mysql 增加浏览量
                rabbitTemplate.convertAndSend(RabbitMqConfig.BLOG_QUEUE, blog);
            } else {
                // 没有缓存 同步操作mysql
                blogDao.updateBlog(blog);
            }
        }
        return blog;
    }


    /**
     * 根据用户查询博文以及标签
     * 正常状态
     *
     * @param page      页数
     * @param showCount 显示数量
     * @return
     */
    public List<Blog> findBlogByUser(Integer page, Integer showCount) {


        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Blog> blogs = blogDao.findBlogByUserId(user.getId(), (page - 1) * showCount, showCount);

        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 查询该用户的博客数量
     * 正常状态
     *
     * @return
     */
    public Long getBlogCountByUser() {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return blogDao.getBlogCountByUserId(user.getId());
    }

    /**
     * 查询主页所有博客数量
     * 正常状态
     *
     * @return
     */
    public Long getHomeBlogCount() {
        return blogDao.getHomeBlogCount();
    }

    /**
     * 查询主页博客
     * 正常状态
     *
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    public List<Blog> findHomeBlog(Integer page, Integer showCount) throws IOException {

        // mysql 分页中的开始位置
        int start = (page - 1) * showCount;

        //没有缓存 需查询mysql 设置缓存
        if (!redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG)) {

            List<Blog> blogsFromMysql = blogDao.findHomeBlog(0, RedisConfig.REDIS_NEW_BLOG_COUNT);
            for (Blog blog : blogsFromMysql) {

                blog.setTags(tagDao.findTagByBlogId(blog.getId()));
                String blogId = Integer.toString(blog.getId());

                redisTemplate.opsForList().rightPush(RedisConfig.REDIS_NEW_BLOG, blogId);
                redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
            }
        }

        // 返回的blog列表
        List<Blog> blogs = new LinkedList<>();

        // /1/5     limit 1,5 1+5=6
        // /5/5     limit 5,5 5+5=10
        //          limit 10,10 10+10=20
        //          limit 6,5 6+5=11

        if (start >= RedisConfig.REDIS_NEW_BLOG_COUNT) {
            // 开始位置大于缓存数量 即查询范围不在缓存内 查询mysql 且不设置缓存
            blogs.addAll(blogDao.findHomeBlog(start, showCount));
            for (Blog blog : blogs) {

                blog.setTags(tagDao.findTagByBlogId(blog.getId()));

            }

        } else if (start + showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
            // 查询范围部分在缓存内
            List<String> redisBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, start, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);
            for (String blogId : redisBlogIds) {
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogs.add(blog);
            }
            blogs.addAll(blogDao.findHomeBlog(RedisConfig.REDIS_NEW_BLOG_COUNT, showCount - (RedisConfig.REDIS_NEW_BLOG_COUNT - start)));

        } else {
            // 查询范围全在缓存
            List<String> redisBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, start, start - 1 + showCount);
            for (String blogId : redisBlogIds) {
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogs.add(blog);
            }

        }

        // 截取前150个字符 减少网络io
        for (Blog blog : blogs) {
            String body = blog.getBody();
            if (body.length() > BlogService.MAX_BODY_CHAR_COUNT) {

                blog.setBody(body.substring(0, BlogService.MAX_BODY_CHAR_COUNT));
            }
        }

        return blogs;


    }

    /**
     * 查询热门博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findHotBlog() throws IOException {
        // 查询redis 热门博客id set

        if (redisTemplate.hasKey(RedisConfig.REDIS_HOT_BLOG)) {

            // 有缓存
            List<Blog> blogList = new ArrayList<>(6);

            List<String> blogIdList = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT);

            for (String blogId : blogIdList) {
                //根据缓存获取 blog
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                // 返回 缓存
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogList.add(blog);
            }


            return blogList;
        } else {
            // redis中没有缓存 查询 mysql
            // 通过定时任务进行热门博客列表更新
            return blogDao.findHotBlog(6);
        }


    }

    /**
     * 搜索博文
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public List<Blog> searchBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogDao.searchBlog(searchText, (page - 1) * showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 符合关键词的博文数量
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public Long getSearchBlogCount(String searchText) {
        return blogDao.getSearchBlogCount(searchText);
    }

    /**
     * 查询所有博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findAllBlog(Integer page, Integer showCount) {
        return blogDao.findAllblog((page - 1) * showCount, showCount);
    }

    /**
     * 修改博文
     *
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限修改");
        }


        blog.setTitle(blogTitle);
        blog.setBody(blogBody);
        blogDao.updateBlog(blog);
        //删除原有标签
        tagDao.deleteTagByBlogId(blog.getId());
        //保存新标签
        for (Integer tagId : tagIds) {
            //保存该博文的标签
            blogDao.saveBlogTag(blog.getId(), tagId);
        }
        // 数据 存在于缓存中
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            blog.setTags(tagDao.findTagByBlogId(blogId));
            redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
        }
    }

    /**
     * 用户删除博文
     *
     * @param blogId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlog(Integer blogId) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);

        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限删除");
        }


        //更改博客状态
        blog.setState(0);
        blogDao.updateBlog(blog);

        //级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);
        // 缓存中有数据
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            // 更新最新博客列表
            blogTask.updateRedisNewBlogList();

        }
        // 删除博客归档信息
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);
    }

    /**
     * 管理员删除博文
     *
     * @param blogId
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteBlog(Integer blogId) throws JsonProcessingException {

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setState(0);
        //更改博客状态
        blogDao.updateBlog(blog);
        //级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);

        // 缓存中有数据
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);
            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);


            if (newBlogIds.contains(blogId + "")) {
                // 更新最新博客列表
                blogTask.updateRedisNewBlogList();
            }

            if (hotBlogIds.contains(blogId + "")) {
                // 更新热门博客列表
                blogTask.updateRedisHotBlogList();
            }
        }
        // 删除博客归档信息
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);

    }

    //存在业务冲突，弃用此方法
//    /**
//     * 撤销删除博文
//     *
//     * @param blogId
//     */
//    public void undoDelete(Integer blogId) {
//        blogDao.updateBlogState(blogId, 1);
//    }


    /**
     * 符合关键字的博文数量
     * 所有状态
     *
     * @param searchText
     * @return
     */
    public Long getSearchAllBlogCount(String searchText) {
        return blogDao.getSearchAllBlogCount(searchText);
    }

    /**
     * 搜索博文
     * 所有状态
     *
     * @param searchText 搜索内容
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> searchAllBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogDao.searchAllBlog(searchText, (page - 1) * showCount, showCount);
        return blogs;
    }


    /**
     * 按月份归档博客
     * 正常状态
     *
     * @return
     */
    public List<Map> statisticalBlogByMonth() throws IOException {

        if (redisTemplate.hasKey(RedisConfig.REDIS_STATISTICAL)) {
            String mapJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_STATISTICAL);
            List<Map> list = objectMapper.readValue(mapJson, List.class);
            return list;
        } else {
            // 设置缓存
            List<Map> maps = blogDao.statisticalBlogByMonth(6);
            redisTemplate.opsForValue().set(RedisConfig.REDIS_STATISTICAL, objectMapper.writeValueAsString(maps));
            return maps;
        }

    }

    /**
     * 查询博客记录数
     * 所有状态
     *
     * @return
     */
    public Long getAllBlogCount() {
        return blogDao.getAllBlogCount();
    }

    /**
     * @Description: 获取用户点赞数
     * @Param: [blogId]
     * @return: int
     * @Author: Tyson
     * @Date: 2020/5/30/0030 14:16
     */
    public int getBlogLikeCountByBlogId(Integer blogId) {
        int likeCount;
        String likeCountKey = String.valueOf(blogId);
        //缓存不存在，查询数据库
        if (!redisTemplate.opsForHash().hasKey(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, likeCountKey)) {
            likeCount =  blogDao.getBlogLikeCountByBlogId(blogId);
            redisTemplate.opsForHash().put(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, likeCountKey, String.valueOf(likeCount));
        } else {
            likeCount = Integer.parseInt((String)redisTemplate.opsForHash().get(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, likeCountKey));
        }
        return likeCount;
    }

    /**
    * @Description: 将Redis博文点赞数存储到数据库，定时任务
    * @Param: []
    * @return: void
    * @Author: Tyson
    * @Date: 2020/5/30/0030 14:22
    */
    @Transactional(rollbackFor = Exception.class)
    public void transLikeCountFromRedis2DB() {
        List<Blog> blogList = redisService.getBlogLikeCountFromRedis();
        for (Blog blog : blogList) {
            int count = blog.getLikeCount();
            blogDao.updateLikeCount(blog.getId(), count);
        }
    }
}
