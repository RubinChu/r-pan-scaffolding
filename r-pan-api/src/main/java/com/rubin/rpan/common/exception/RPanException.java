package com.rubin.rpan.common.exception;

import com.rubin.rpan.common.response.ResponseCode;
import lombok.Data;

/**
 * 自定义异常
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
public class RPanException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

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

}
