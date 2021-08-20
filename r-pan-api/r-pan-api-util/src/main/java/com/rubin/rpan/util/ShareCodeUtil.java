package com.rubin.rpan.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 分享码生成器
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class ShareCodeUtil {

    /**
     * 分享码长度
     */
    private static final Integer SHARE_CODE_LENGTH = 4;

    /**
     * 生成分享码
     *
     * @return
     */
    public static String get() {
        return RandomStringUtils.randomAlphanumeric(SHARE_CODE_LENGTH).toLowerCase();
    }

}
