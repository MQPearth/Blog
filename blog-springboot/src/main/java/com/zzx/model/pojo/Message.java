package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 留言
 */
@Data
@ToString
public class Message {
    private Integer id;//id
    private String name;//游客显示为ip地址
    private String body;//留言内容
    private Date time;//留言时间


}
