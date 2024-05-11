package com.zzx.enums;

public enum BlogTabelEnum {


    ALL(0, "所有标签"),
    BUG(1, "BUG标签"),
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    BlogTabelEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}