package com.zzx.model.entity;

/**
 * 状态码
 */
public class StatusCode {
    /**
     * 操作成功
     */
    public static final int OK = 200;
    /**
     * 失败
     */
    public static final int ERROR = 201;
    /**
     * 用户名或密码错误
     */
    public static final int LOGINERROR = 202;
    /**
     * token过期
     */
    public static final int TOKENEXPIREE = 203;
    /**
     * 权限不足
     */
    public static final int ACCESSERROR = 403;
    /**
     * 远程调用失败
     */
    public static final int REMOTEERROR = 204;
    /**
     * 重复操作
     */
    public static final int REPERROR = 205;
    /**
     * 业务层错误
     */
    public static final int SERVICEERROR = 500;
    /**
     * 资源不存在
     */
    public static final int NOTFOUND = 404;


}
