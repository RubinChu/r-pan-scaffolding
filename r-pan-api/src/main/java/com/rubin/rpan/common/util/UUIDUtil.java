package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constant.CommonConstant;

import java.util.UUID;

/**
 * UUID工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class UUIDUtil {

    /**
     * 获取UUID字符串
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace(CommonConstant.HYPHEN_STR, CommonConstant.EMPTY_STR).toUpperCase();
    }

}
