package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 分享ID工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class ShareIdUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前查看的分享ID
     *
     * @param value
     */
    public static void set(Long value) {
        threadLocal.set(value);
    }

    /**
     * 获取当前查看的分享ID
     *
     * @return
     */
    public static Long get() {
        Long value = threadLocal.get();
        if (Objects.isNull(value)) {
            return CommonConstant.ZERO_LONG;
        }
        return value;
    }

}
