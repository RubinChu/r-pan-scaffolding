package com.rubin.rpan.util;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class MD5Util {

    public static final String UTF_8_STR = "UTF-8";
    private static final String MD5_STR = "MD5";
    public static final String ZERO_STR = "0";

    /**
     * 获取md5加密串
     *
     * @param message
     * @return
     */
    public static String getMD5(String message) {
        String md5 = "";
        try {
            // 创建一个md5算法对象
            MessageDigest md = MessageDigest.getInstance(MD5_STR);
            byte[] messageByte = message.getBytes(UTF_8_STR);
            // 获得MD5字节数组,16 * 8 = 128位
            byte[] md5Byte = md.digest(messageByte);
            // 转换为16进制字符串
            md5 = bytesToHex(md5Byte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append(ZERO_STR);
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

}
