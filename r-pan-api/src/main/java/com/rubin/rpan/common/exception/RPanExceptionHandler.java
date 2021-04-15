package com.rubin.rpan.common.exception;

import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.response.R;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常捕获
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@RestControllerAdvice
public class RPanExceptionHandler {

    @ExceptionHandler(value = RPanException.class)
    public R rPanExceptionHandler(RPanException e) {
        return R.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().get();
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public R constraintViolationExceptionHandler(ConstraintViolationException e) {
        ConstraintViolation constraintViolation = e.getConstraintViolations().stream().findFirst().get();
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), constraintViolation.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), ResponseCode.ERROR_PARAM.getDesc());
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public R illegalStateExceptionHandler(IllegalStateException e) {
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), ResponseCode.ERROR_PARAM.getDesc());
    }

}
