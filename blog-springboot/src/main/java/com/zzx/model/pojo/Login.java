package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 登录表
 */
@Data
@ToString
public class Login {

    private Date time;//最后登录时间
    private String ip;//最后登录ip
    private User user;//用户


}
