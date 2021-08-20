package com.rubin.rpan.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 公用返回对象
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
// 保证json序列化的时候，如果value为null的时候，key也会消失
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {
    private int status;
    private String message;
    private T data;

    private R(int status) {
        this.status = status;
    }

    private R(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private R(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private R(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 使之在json序列化的结果当中
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> R<T> success() {
        return new R<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> R<T> success(String message) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> R<T> data(T data) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> R<T> fail() {
        return new R<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> R<T> fail(String error_message) {
        return new R<T>(ResponseCode.ERROR.getCode(), error_message);
    }

    public static <T> R<T> fail(int error_code, String error_message) {
        return new R<T>(error_code, error_message);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}
