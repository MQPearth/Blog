package com.zzx.model.pojo;

import java.util.Date;

/**
 * 登录表
 */
public class Login {

    private Date time;//最后登录时间
    private String ip;//最后登录ip
    private User user;//用户

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
