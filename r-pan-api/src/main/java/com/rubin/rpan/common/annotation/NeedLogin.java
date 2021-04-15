package com.rubin.rpan.common.annotation;

import java.lang.annotation.*;

/**
 * 接口需要登录url标识注解
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedLogin {
}
