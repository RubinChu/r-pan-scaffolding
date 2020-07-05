package com.rubin.rpan.common.exception;

import com.rubin.rpan.common.response.ServerResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception capture
 * Created by rubin on 2020/5/30.
 */

@RestControllerAdvice
public class RPanExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ServerResponse rPanExceptionHandler(Exception e) {
        return ServerResponse.createByErrorMessage(e.getMessage());
    }

}
