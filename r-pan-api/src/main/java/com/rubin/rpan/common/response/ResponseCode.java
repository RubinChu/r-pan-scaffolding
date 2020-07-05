package com.rubin.rpan.common.response;

/**
 * Public return code enumeration class
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    TOKEN_EXPIRE(2, "TOKEN_EXPIRE");

    private int code;
    private String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
