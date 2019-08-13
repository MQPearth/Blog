package com.zzx.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 博文
 */
@Data
@ToString(exclude = "body")
public class Blog implements Serializable {
    /**
     * blog(36) => 541312(10)
     */
    private static final long serialVersionUID = 541312L;

    private Integer id;//id
    private String title;//标题
    private String body;//内容
    private Integer discussCount;//评论数
    private Integer blogViews;//浏览数
    private Date time;//发布时间
    private Integer state;//博文状态--0删除 1正常


    private User user;//用户

    private List<Tag> tags;//博文对应的标签


}
