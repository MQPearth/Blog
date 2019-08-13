package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 公告
 */
@Data
@ToString

public class Announcement {
    private Integer id;//id
    private String title;//公告标题
    private String body;//公告内容
    private Integer top;//是否置顶0 置顶 1未置顶
    private Date time;//发布时间




}
