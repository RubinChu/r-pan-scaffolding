package com.rubin.rpan.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Public constant class
 * Created by rubin on 2019/9/7.
 */

public class Constants {

    public static final String NULL_STR = "null";

    public static final String CONTENT_TYPE_STR = "content-type";
    public static final String APPLICATION_OCTET_STREAM_STR = "application/octet-stream";
    public static final String CONTENT_DISPOSITION_STR = "Content-Disposition";
    public static final String CONTENT_DISPOSITION_VALUE_PREFIX_STR = "attachment;fileName=";
    public static final String GB2312_STR = "GB2312";
    public static final String IOS_8859_1_STR = "ISO-8859-1";
    public static final String ALL_FILE_CN_STR = "全部文件";

    public static final String ALL_URI = "/**";

    public static final String UTF_8_STR = "UTF-8";
    public static final String APPLICATION_JSON_STR = "application/json; charset=utf-8";

    public static final String COMMON_SEPARATOR = "__,__";
    public static final String EMPTY_STR = "";
    public static final String POINT_STR = ".";
    public static final String SEPARATOR_STR = File.separator;
    public static final String HYPHEN_STR = "-";
    public static final String CN_LEFT_PARENTHESES_STR = "（";
    public static final String CN_RIGHT_PARENTHESES_STR = "）";
    public static final String ZIP_FILE_NAME_SUFFIX_STR = ".zip";

    public static final Integer ZERO_INT = 0;
    public static final Integer ONE_INT = 1;
    public static final Integer MINUS_ONE_INT = -1;

    public final static String TOKEN_KEY = "Authorization";
    public final static String JWT_PRIVATE_KEY = "0123456789";
    public final static String RENEWAL_TIME = "RENEWAL_TIME";
    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";

    public static final List<String> WHITE_LIST = Arrays.asList("/user/login", "/user/register", "/user/username/check", "/user/answer/check", "/user/password/reset", "/file/download", "/file/zip/download");
    public static final String MD5_STR = "MD5";
    public static final String ZERO_STR = "0";

    public static final String USER_LOGIN_PREFIX = "USER_LOGIN_";

    public static final String ORIGIN_FILE_REAL_PATH_STR = "originFileRealPath";
    public static final String NEW_FILE_REAL_PATH_STR = "newFileRealPath";

    public static final String TOP_STR = "TOP";

    public static final String KB_STR = "K";
    public static final String MB_STR = "M";
    public static final String GB_STR = "G";
    public static final String MINUS_ONE_STR = "-1";

    /**
     * file type
     */
    @AllArgsConstructor
    @Getter
    public enum FileType {
        ALL(-1, "所有文件"),
        DIRECTORY(0, "文件夹"),
        FILE(1, "文件"),
        ARCHIVE(2, "压缩文件"),
        EXCEL(3, "EXCEL"),
        WORD(4, "WORD"),
        PDF(5, "PDF"),
        TXT(6, "TXT"),
        IMG(7, "图片"),
        AUDIO(8, "音频"),
        VIDEO(9, "视频"),
        POWERPOINT(10, "PPT"),
        CODE(11, "源码文件");
        Integer code;
        String name;
    }


}
