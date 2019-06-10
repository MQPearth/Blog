package com.zzx.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 标签
 */
public class Tag {
    private Integer id;//id
    private String name;//标签名

    @JsonIgnore
    private User user;//用户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
