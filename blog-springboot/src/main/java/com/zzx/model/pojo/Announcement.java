package com.zzx.model.pojo;

/**
 * 公告
 */
public class Announcement {
    private Integer id;//id
    private String title;//公告标题
    private String body;//公告内容
    private Integer top;//是否置顶0 置顶 1未置顶

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

}
