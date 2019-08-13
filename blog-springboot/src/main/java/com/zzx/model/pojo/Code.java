package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 邀请码
 */
@Data
@ToString
public class Code {
    private String id;//id
    private Integer state;//状态 0 未使用 1已使用 2 已删除
    private User user;



}
