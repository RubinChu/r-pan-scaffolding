package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constants.Constants;

import java.security.MessageDigest;

/**
 * @Description MD5 encryption tools
 * @auther chuqian
 * @create 2019-09-19 17:05
 */
public class MD5Util {

    /**
     * Get md5 encrypted string
     *
     * @param message
     * @return
     */
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance(Constants.MD5_STR);
            byte[] messageByte = message.getBytes(Constants.UTF_8_STR);
            byte[] md5Byte = md.digest(messageByte);
            md5 = bytesToHex(md5Byte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * Binary to hexadecimal
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
                hexStr.append(Constants.ZERO_STR);
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
