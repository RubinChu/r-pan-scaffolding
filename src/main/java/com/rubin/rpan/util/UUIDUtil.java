package com.rubin.rpan.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @Description UUID工具类
 * @auther rubin
 * @create 2019-09-19 17:06
 */
public class UUIDUtil {

    /**
     * 获取UUID字符串
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
