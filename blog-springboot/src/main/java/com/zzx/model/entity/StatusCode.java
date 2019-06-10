package com.zzx.model.entity;

/**
 * 状态码实体类
 */
public class StatusCode {
    public static final int OK = 200;//成功
    public static final int ERROR = 201;//失败
    public static final int LOGINERROR = 202;//用户名或密码错误
    public static final int TOKENEXPIREE = 203;//token过期
    public static final int ACCESSERROR = 403;//权限不足
    public static final int REMOTEERROR = 204;//远程调用失败
    public static final int REPERROR = 205;//重复操作
    public static final int SERVICEERROR = 500;//业务层错误
    public static final int NOTFOUND = 404;//资源不存在

}
