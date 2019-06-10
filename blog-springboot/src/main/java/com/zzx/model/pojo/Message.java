package com.zzx.model.pojo;

/**
 * 留言
 */
public class Message {
    private Integer id;//id
    private String name;//游客显示为ip地址
    private String body;//留言内容

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
