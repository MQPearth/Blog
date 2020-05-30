package com.zzx.service;

import com.zzx.config.RedisConfig;
import com.zzx.model.pojo.Blog;
import com.zzx.model.pojo.UserLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Tyson
 * @time: 2020-05-29 00:07
 */
@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
    * @Description: 从Redis获取用户点赞数据
    * @Param: []
    * @return: java.util.List<com.zzx.model.pojo.UserLike>
    * @Author: Tyson
    * @Date: 2020/5/30/0030 11:37
    */
    public List<UserLike> getUserLikeFromRedis() {
        List<UserLike> userLikeList = new ArrayList<>();
        try {
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConfig.MAP_USER_LIKE_KEY, ScanOptions.NONE);
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();

                String key = (String)entry.getKey();
                //拆分key，blogId::userId
                String[] keyArr = key.split("::");
                int blogId  = Integer.parseInt(keyArr[0]);
                int userId= Integer.parseInt(keyArr[1]);
                int status = Integer.parseInt((String)entry.getValue());

                UserLike userLike = new UserLike(blogId, userId, status);

                userLikeList.add(userLike);
                //从redis删除key
                redisTemplate.opsForHash().delete(RedisConfig.MAP_USER_LIKE_KEY, key);
            }
            cursor.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userLikeList;
    }

    /**
    * @Description: 从Redis获取博文点赞数
    * @Param: []
    * @return: java.util.List<com.zzx.model.pojo.Blog>
    * @Author: Tyson
    * @Date: 2020/5/30/0030 11:35
    */
    public List<Blog> getBlogLikeCountFromRedis() {
        List<Blog> blogList = new ArrayList<>();
        try {
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, ScanOptions.NONE);
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();

                String key = (String)entry.getKey();
                int blogId = Integer.parseInt(key);
                int count = Integer.parseInt((String)entry.getValue());

                Blog blog = new Blog(blogId, count);

                blogList.add(blog);
                //从redis删除key
                redisTemplate.opsForHash().delete(RedisConfig.MAP_BLOG_LIKE_COUNT_KEY, key);
            }
            cursor.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return blogList;
    }
}