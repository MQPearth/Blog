package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 评论
 */
@Data
@ToString
public class Discuss {
    private Integer id;//id
    private String body;//评论内容
    private Date time;//评论时间
    private User user;//评论用户
    private Blog blog;//评论博文
    private List<Reply> replyList;



}
