package com.rubin.rpan.common.response;

/**
 * 公用返回码枚举类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 错误
     */
    ERROR(1, "ERROR"),
    /**
     * 需要登录
     */
    NEED_LOGIN(10, "NEED_LOGIN"),
    /**
     * token过期
     */
    TOKEN_EXPIRE(2, "TOKEN_EXPIRE"),
    /**
     * 需要分享码
     */
    NEED_SHARE_CODE(4, "NEED_SHARE_CODE"),
    /**
     * 分享的文件丢失
     */
    SHARE_FILE_MISS(5, "分享的文件丢失"),
    /**
     * 分享已取消
     */
    SHARE_CANCELLED(6, "分享已取消"),
    /**
     * 分享已过期
     */
    SHARE_EXPIRE(7, "分享已过期"),
    /**
     * 参数错误
     */
    ERROR_PARAM(3, "ERROR_PARAM");

    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
