package com.zzx.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 将一些不方便加@Component注解的类放在此处
 * 加入spring容器
 */

@Component
public class BeanConfig {


    /**
     * spring-security加密方法
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * spring-boot内置的json工具
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    /**
     * 新建一个用于发送邮件的消息队列
     *
     * @return
     */
    @Bean
    public Queue sendSmsQueue() {
        return new Queue(RabbitMqConfig.MAIL_QUEUE);
    }

    /**
     * 新建一个用于更新博客的消息队列
     *
     * @return
     */
    @Bean
    public Queue updateBlogQueue() {
        return new Queue(RabbitMqConfig.BLOG_QUEUE);
    }


}
