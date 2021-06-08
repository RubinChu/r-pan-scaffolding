package com.rubin.rpan.common.constant;

/**
 * 公用常量类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class CommonConstant {

    public static final String UTF_8_STR = "UTF-8";
    public static final String COMMON_SEPARATOR = "__,__";
    public static final String EMPTY_STR = "";
    public static final String POINT_STR = ".";
    public static final String HYPHEN_STR = "-";
    public static final Long ZERO_LONG = 0L;
    public static final Long TWO_LONG = 2L;
    public static final String ZERO_STR = "0";
    public static final Integer ZERO_INT = 0;
    public static final Integer ONE_INT = 1;
    public static final Integer TWO_INT = 2;
    public static final Integer MINUS_ONE_INT = -1;
    public static final String TRUE_STR = "true";

    public static final Long ONE_DAY_LONG = 24L * 60L * 60L * 1000L;
    public static final Long FIVE_MINUTES_LONG = 5L * 60L * 1000L;
    public static final Long ONE_HOUR_LONG = 60L * 60L * 1000L;

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

    /**
     * 用户校验分享码通过之后存储redis的key前缀
     */
    public static final String SHARE_CODE_PREFIX = "SHARE_CODE_";

    /**
     * 跨域设置枚举类
     */
    public enum CorsConfigEnum {
        /**
         * 允许所有远程访问
         */
        CORS_ORIGIN("Access-Control-Allow-Origin", "*"),
        /**
         * 允许认证
         */
        CORS_CREDENTIALS("Access-Control-Allow-Credentials", "true"),
        /**
         * 允许远程调用的请求类型
         */
        CORS_METHODS("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"),
        /**
         * 指定本次预检请求的有效期，单位是秒
         */
        CORS_MAX_AGE("Access-Control-Max-Age", "3600"),
        /**
         * 允许所有请求头
         */
        CORS_HEADERS("Access-Control-Allow-Headers", "*");

        CorsConfigEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
