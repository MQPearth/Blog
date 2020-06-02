package com.zzx.service;

import com.zzx.config.RedisConfig;
import com.zzx.dao.BlogDao;
import com.zzx.dao.UserDao;
import com.zzx.dao.UserLikeDao;
import com.zzx.model.pojo.User;
import com.zzx.model.pojo.UserLike;
import com.zzx.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Tyson
 * @time: 2020-05-29 00:07
 */
@Service
public class UserLikeService {

    @Autowired
    private UserLikeDao userLikeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * @Description: 保存用户点赞数据
     * @Param: [userLike]
     * @return: void
     * @Author: Tyson
     * @Date: 2020/5/30/0030 11:39
     */
    public void saveUserLike(UserLike userLike) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        int blogId = userLike.getBlog().getId();
        String userLikeKey = getUserLikeKey(blogId, user.getId());
        //用户点赞状态存进Redis，key为blogId::userId，value为点赞状态status
        redisTemplate.opsForHash().put(RedisConfig.MAP_USER_LIKE_KEY, userLikeKey, String.valueOf(userLike.getStatus()));

        int val = userLike.getStatus() == 0 ? -1 : 1;
        String blogLikeCountKey = String.valueOf(blogId);
        //缓存不存在，查询数据库，然后存入Redis，key为blogId，value为点赞数
        if (!redisTemplate.opsForHash().hasKey(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, blogLikeCountKey)) {
            int likeCount = blogDao.getBlogLikeCountByBlogId(blogId);
            redisTemplate.opsForHash().put(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, blogLikeCountKey, String.valueOf(likeCount + val));
        } else {
            redisTemplate.opsForHash().increment(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, blogLikeCountKey, val);
        }
    }

    /**
     * @Description: 用户是否点过赞
     * @Param: [blogId]
     * @return: boolean
     * @Author: Tyson
     * @Date: 2020/5/30/0030 14:09
     */
    public boolean getUserLike(Integer blogId) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        String userLikeKey = getUserLikeKey(blogId, user.getId());
        int status = 0;
        //缓存不存在，先查询数据库
        if (!redisTemplate.opsForHash().hasKey(RedisConfig.MAP_USER_LIKE_KEY, userLikeKey)) {
            //key为blogId::userId
            UserLike userLike = userLikeDao.getUserLike(blogId, user.getId());
            if (userLike != null) {
                status = userLike.getStatus();
            }
            redisTemplate.opsForHash().put(RedisConfig.MAP_USER_LIKE_KEY, userLikeKey, String.valueOf(status));
        } else {
            status = Integer.parseInt((String)redisTemplate.opsForHash().get(RedisConfig.MAP_USER_LIKE_KEY, userLikeKey));
        }
        return status != 0;
    }

    /**
     * @Description: 将Redis点赞状态存储到数据库，定时任务
     * @Param: []
     * @return: void
     * @Author: Tyson
     * @Date: 2020/5/30/0030 14:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void transUserLikeFromRedis2DB() {
        List<UserLike> userLikeList = redisService.getUserLikeFromRedis();
        for (UserLike ul : userLikeList) {
            userLikeDao.saveUserLike(ul);
        }
    }

    /**
     * @Description: 用户点赞状态key
     * @Param: [userId, blogId]
     * @return: java.lang.String
     * @Author: Tyson
     * @Date: 2020/5/30/0030 14:21
     */
    public static String getUserLikeKey(int blogId, int userId) {
        StringBuilder builder = new StringBuilder();
        builder.append(blogId);
        builder.append(RedisConfig.REDIS_LIKE_MID);
        builder.append(userId);
        return builder.toString();
    }
}
