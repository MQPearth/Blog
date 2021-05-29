package com.zzx.config;

/**
 * @blame MQPearth
 */
public class RabbitMqConfig {
    /**
     * 邮件消息队列 名
     */
    public static final String MAIL_QUEUE = "MAIL";

    /**
     * 博客数据更新 消息队列 名
     */
    public static final String BLOG_QUEUE = "BLOG";

    /**
     * controller日志log
     */
    public static final String CONTROLLER_LOG_QUEUE = "CONTROLLER_LOG";


    /**
     * sql日志log
     */
    public static final String SQL_LOG_QUEUE = "SQL_LOG";
}
