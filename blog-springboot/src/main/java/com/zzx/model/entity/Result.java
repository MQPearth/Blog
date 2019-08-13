package com.zzx.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 返回结果实体类
 */

@ApiModel("响应实体")

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
public class Result {

    @ApiModelProperty(value = "返回码", dataType = "Integer")
    private Integer code;// 返回码

    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String message;//返回信息

    @ApiModelProperty(value = "返回数据", dataType = "object")
    private Object data;// 返回数据

    private Result() {
    }


    private Result(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    private Result(Integer code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result create(Integer code, String message) {
        return new Result(code, message);
    }

    public static Result create(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }





}
