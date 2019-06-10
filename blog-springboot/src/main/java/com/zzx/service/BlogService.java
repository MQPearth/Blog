package com.zzx.service;

import com.zzx.config.ImgUploadConfig;
import com.zzx.dao.BlogDao;
import com.zzx.dao.RoleDao;
import com.zzx.dao.TagDao;
import com.zzx.dao.UserDao;
import com.zzx.model.pojo.Blog;
import com.zzx.model.pojo.User;
import com.zzx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private ImgUploadConfig imgUploadConfig;

    @Autowired
    private HttpServletRequest request;


    /**
     * 保存图片,返回url
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImg(MultipartFile file) throws IOException {

        //获取图片格式/后缀
        String format = formatUtil.getFileFormat(file.getOriginalFilename());
        //获取图片保存路径
        String savePath = fileUtil.getSavePath();
        if (!formatUtil.checkStringNull(savePath))//存储已满
            throw new IOException("存储已满 请联系管理员");
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
    @Transactional
    public void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = new Blog();
        blog.setUser(user);//博文用户
        blog.setBlogViews(0);//浏览量
        blog.setDiscussCount(0);//评论数
        blog.setTitle(blogTitle);//标题
        blog.setBody(blogBody);//内容
        blog.setState(1);//1 正常状态
        blog.setTime(dateUtil.getCurrentDate());//发布时间
        blogDao.saveBlog(blog);

        for (Integer tagId : tagIds) {
            blogDao.saveBlogTag(blog.getId(), tagId);//保存该博文的标签
        }
    }

    /**
     * 根据id查询博文以及博文标签
     * 正常状态
     *
     * @param blogId
     * @return
     */
    public Blog findBlogById(Integer blogId) {
        Blog blog = blogDao.findBlogById(blogId);
        if (blog == null)
            throw new RuntimeException("博客不存在");
        blog.setTags(tagDao.findTagByBlogId(blogId));
        blog.setBlogViews(blog.getBlogViews() + 1);
        blogDao.updateBlogViews(blog);
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
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> findHomeBlog(Integer page, Integer showCount) {
        List<Blog> blogs = blogDao.findHomeBlog((page - 1) * showCount, showCount);

        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 查询热门博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findHotBlog() {
        return blogDao.findHotBlog(6);
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
    @Transactional
    public void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);
        if (user.getId() != blog.getUser().getId())
            throw new RuntimeException("无权限修改");

        blog.setTitle(blogTitle);
        blog.setBody(blogBody);
        blogDao.updateBlog(blog);
        //删除原有标签
        tagDao.deleteTagByBlogId(blog.getId());
        //保存新标签
        for (Integer tagId : tagIds) {
            blogDao.saveBlogTag(blog.getId(), tagId);//保存该博文的标签
        }
    }

    /**
     * 用户删除博文
     *
     * @param blogId
     */
    @Transactional
    public void deleteBlog(Integer blogId) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);
//        user.setRoles(roleDao.findUserRoles(user.getId()));
        if (user.getId() != blog.getUser().getId())
            throw new RuntimeException("无权限删除");

        blogDao.updateBlogState(blogId, 0);//更改博客状态

        //级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);
    }

    /**
     * 管理员删除博文
     *
     * @param blogId
     */
    @Transactional
    public void adminDeleteBlog(Integer blogId) {

        blogDao.updateBlogState(blogId, 0);//更改博客状态
        //级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);
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
    public List<Map> statisticalBlogByMonth() {
        return blogDao.statisticalBlogByMonth(6);
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
}
