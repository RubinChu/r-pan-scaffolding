package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户ID工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class UserIdUtil {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录的用户ID
     *
     * @param value
     */
    public static void set(String value) {
        threadLocal.set(value);
    }

    /**
     * 获取当前登录的用户ID
     *
     * @return
     */
    public static String get() {
        String value = threadLocal.get();
        if (StringUtils.isBlank(value)) {
            return CommonConstant.EMPTY_STR;
        }
        return value;
    }

}
