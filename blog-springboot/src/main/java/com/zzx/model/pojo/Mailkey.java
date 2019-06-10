package com.zzx.model.pojo;

import java.util.Date;

/**
 * 邮箱验证码
 */
public class Mailkey {
    private Integer id;
    private String mail;//发送邮箱
    private String code;//验证码
    private Date sendTime;//发送时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
