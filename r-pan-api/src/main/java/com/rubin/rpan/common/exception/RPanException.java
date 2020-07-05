package com.rubin.rpan.common.exception;

/**
 * Custom exception
 * Created by rubin on 2020/5/30.
 */

public class RPanException extends RuntimeException {

    public RPanException() {
    }

    public RPanException(String message) {
        super(message);
    }

    public RPanException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPanException(Throwable cause) {
        super(cause);
    }

    public RPanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
