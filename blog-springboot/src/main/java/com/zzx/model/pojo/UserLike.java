package com.zzx.model.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 用户点赞状态
 * @author: Tyson
 * @time: 2020-05-29 00:00
 */
@Data
@ToString
@ApiModel("用户")
public class UserLike implements Serializable {

    private static final long serialVersionUID = -3956628880213302317L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 点赞的用户
     */
    private User user;

    /**
     * 被点赞的用户的博文
     */
    private Blog blog;

    /**
     * 点赞的状态.默认未点赞
     */
    private Integer status = 0;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UserLike() {
    }

    public UserLike(int blogId, int userId, int status) {
        Blog blog = new Blog();
        blog.setId(blogId);

        User user = new User();
        user.setId(userId);

        this.blog = blog;
        this.user = user;
        this.status = status;
    }
}

