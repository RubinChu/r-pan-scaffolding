package com.rubin.rpan.util;

import java.util.Objects;

/**
 * 用户ID工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class UserIdUtil {

    public static final Long ZERO_LONG = 0L;

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录的用户ID
     *
     * @param value
     */
    public static void set(Long value) {
        threadLocal.set(value);
    }

    /**
     * 获取当前登录的用户ID
     *
     * @return
     */
    public static Long get() {
        Long value = threadLocal.get();
        if (Objects.isNull(value)) {
            return ZERO_LONG;
        }
        return value;
    }

}
