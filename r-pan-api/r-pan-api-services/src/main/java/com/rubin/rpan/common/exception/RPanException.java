package com.rubin.rpan.common.exception;

import com.rubin.rpan.common.response.ResponseCode;

import java.util.Objects;

/**
 * 自定义异常
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    public RPanException() {
    }

    public RPanException(String message) {
        this.code = ResponseCode.ERROR.getCode();
        this.msg = message;
    }

    public RPanException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RPanException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getDesc();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanException that = (RPanException) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg);
    }

    @Override
    public String toString() {
        return "RPanException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
