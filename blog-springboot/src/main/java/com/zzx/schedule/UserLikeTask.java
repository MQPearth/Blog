package com.zzx.schedule;

import com.zzx.service.BlogService;
import com.zzx.service.UserLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 定时将Redis点赞相关数据写进数据库
 * @author: Tyson
 * @time: 2020-05-28 23:57
 */
@Component
@Configuration
@EnableScheduling
@Slf4j
public class UserLikeTask {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserLikeService userLikeService;

    /**
    * @Description: 2小时执行一次
    * @Param: []
    * @return: void
    * @Author: Tyson
    * @Date: 2020/5/30/0030 14:51
    */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 2)
    private void userLikeTask() {
        log.info("execute userLike task");
        blogService.transLikeCountFromRedis2DB();
        userLikeService.transUserLikeFromRedis2DB();
    }

}