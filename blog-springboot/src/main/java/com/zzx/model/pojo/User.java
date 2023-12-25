package com.zzx.model.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 用户
 */
@Data
@ToString
@ApiModel("用户")
public class User implements Serializable {

    /**
     * user(36) => 1436499(10)
     */
    private static final long serialVersionUID = 1436499L;


    @ApiModelProperty(value = "用户id", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(value = "用户名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String mail;

    @ApiModelProperty(value = "用户状态", dataType = "Integer")
    private Integer state;

    @ApiModelProperty(value = "打赏码路径", dataType = "String")
    private String reward;

    @ApiModelProperty(value = "头像路径", dataType = "String")
    private String icon;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Role> roles;

    private Login login;


}