package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 按代码规范, 包名应该为po, 历史遗留代码, 就不改了
 * 公告
 */
@Data
@ToString
public class Announcement {
    //id
    private Integer id;
    //公告标题
    private String title;
    //公告内容
    private String body;
    //是否置顶0 置顶 1未置顶
    private Integer top;
    //发布时间
    private Date time;




}
