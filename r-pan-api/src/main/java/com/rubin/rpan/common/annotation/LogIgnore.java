package com.rubin.rpan.common.annotation;

import java.lang.annotation.*;

/**
 * 标注不打印接口日志
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogIgnore {
}
