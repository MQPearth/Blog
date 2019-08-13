package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 回复
 */
@Data
@ToString

public class Reply {
    private Integer id;//id
    private String body;//回复内容
    private Date time;//回复时间
    private User user;//用户
    private Discuss discuss;//评论
    private Reply reply;//父节点回复




}
