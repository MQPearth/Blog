package com.zzx.config;

/**
 * 配置redis的存取
 *
 * @blame mqpearth
 */
public class RedisConfig {

    /**
     * redis中存放 最新博客 数量 的最大值
     */
    public static final int REDIS_NEW_BLOG_COUNT = 10;

    /**
     * redis中存放 热门博客 数量 的最大值
     */
    public static final int REDIS_HOT_BLOG_COUNT = 6;

    /**
     * redis中存放 热门博客 的 key
     */
    public static final String REDIS_HOT_BLOG = "HOTBLOG";


    /**
     * redis中存放 最新博客 的 key
     */
    public static final String REDIS_NEW_BLOG = "NEWBLOG";

    /**
     * redis中存放blog的前缀
     */
    public static final String REDIS_BLOG_PREFIX = "BLOG_";


    /**
     * 博客归档缓存key
     */
    public static final String REDIS_STATISTICAL = "STATISTICAL";

    /**
     * IP_127.0.0.1
     */
    public static final String REDIS_IP_PREFIX = "IP_";


    /**
     * 请求频率限制 缓存时间
     */
    public static final long REDIS_LIMIT_REQUEST_FREQUENCY_TIME = 100L;

    /**
     * 用户点赞状态key
     */
    public static final String MAP_BLOG_LIKE_COUNT_KEY = "MAP_BLOG_LIKE_COUNT";

    /**
     * MAP_USER_LIKE_KEY 行内key 的中间字符
     * blogId::userId
     */
    public static final String REDIS_LIKE_MID = "::";

    /**
     * 博文点赞数key
     */
    public static final String MAP_USER_LIKE_KEY = "MAP_USER_LIKE_KEY";

}