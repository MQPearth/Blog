package com.zzx.model.pojo;

/**
 * 邀请码
 */

public class Code {
    private String id;//id
    private Integer state;//状态 0 未使用 1已使用 2 已删除
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
