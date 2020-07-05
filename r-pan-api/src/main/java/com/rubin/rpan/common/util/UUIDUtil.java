package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constants.Constants;

import java.util.UUID;

/**
 * @Description UUID tools
 * @auther chuqian
 * @create 2019-09-19 17:06
 */
public class UUIDUtil {

    /**
     * Get UUID string
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace(Constants.HYPHEN_STR, Constants.EMPTY_STR).toUpperCase();
    }
}
