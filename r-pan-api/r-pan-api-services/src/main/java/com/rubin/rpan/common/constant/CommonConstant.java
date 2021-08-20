package com.rubin.rpan.common.constant;

/**
 * 公用常量类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class CommonConstant {

    public static final String COMMON_SEPARATOR = "__,__";
    public static final String EMPTY_STR = "";
    public static final String POINT_STR = ".";
    public static final String SLASH_STR = "/";
    public static final Long ZERO_LONG = 0L;
    public static final Integer ZERO_INT = 0;
    public static final Integer ONE_INT = 1;
    public static final Integer TWO_INT = 2;
    public static final Integer SEVEN_INT = 7;
    public static final Integer MINUS_ONE_INT = -1;
    public static final String TRUE_STR = "true";

    public static final Long ONE_DAY_LONG = 24L * 60L * 60L * 1000L;
    public static final Long FIVE_MINUTES_LONG = 5L * 60L * 1000L;
    public static final Long ONE_HOUR_LONG = 60L * 60L * 1000L;

    public static final String DEFAULT_SHARE_PREFIX = "http://127.0.0.1:7001/share/";

    /**
     * 登录用户ID的key
     */
    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";

    /**
     * 分享ID
     */
    public static final String SHARE_ID = "SHARE_ID";

    /**
     * 用户登录后存储redis的key前缀
     */
    public static final String USER_LOGIN_PREFIX = "USER_LOGIN_";

}
