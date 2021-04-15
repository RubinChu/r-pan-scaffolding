package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件工具类
 * Created by RubinChu on 2021/1/22 下午 3:07
 */
public class FileUtil {

    private static final String KB_STR = "K";
    private static final String MB_STR = "M";
    private static final String GB_STR = "G";
    private static final Integer UNIT = 1024;
    private static final String FILE_SIZE_DESC_FORMAT = "%.2f";


    /**
     * 获取文件大小字符串
     *
     * @param size
     * @return
     */
    public static String getFileSizeDesc(long size) {
        double fileSize = (double) size;
        String fileSizeSuffix = KB_STR;
        fileSize = fileSize / UNIT;
        if (fileSize > UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = MB_STR;
        }
        if (fileSize > UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = GB_STR;
        }
        return String.format(FILE_SIZE_DESC_FORMAT, fileSize) + fileSizeSuffix;
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName) || (fileName.indexOf(CommonConstant.POINT_STR) == CommonConstant.MINUS_ONE_INT)) {
            return CommonConstant.EMPTY_STR;
        }
        return fileName.substring(fileName.lastIndexOf(CommonConstant.POINT_STR)).toLowerCase();
    }

}
