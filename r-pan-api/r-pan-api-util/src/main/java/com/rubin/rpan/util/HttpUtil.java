package com.rubin.rpan.util;

import javax.servlet.http.HttpServletResponse;

/**
 * Http工具类
 * Created by RubinChu on 2021/1/19 17:54
 */
public class HttpUtil {

    /**
     * 添加跨域的响应头
     *
     * @param response
     */
    public static void addCorsResponseHeader(HttpServletResponse response) {
        response.setHeader(CorsConfigEnum.CORS_ORIGIN.getKey(), CorsConfigEnum.CORS_ORIGIN.getValue());
        response.setHeader(CorsConfigEnum.CORS_CREDENTIALS.getKey(), CorsConfigEnum.CORS_CREDENTIALS.getValue());
        response.setHeader(CorsConfigEnum.CORS_METHODS.getKey(), CorsConfigEnum.CORS_METHODS.getValue());
        response.setHeader(CorsConfigEnum.CORS_MAX_AGE.getKey(), CorsConfigEnum.CORS_MAX_AGE.getValue());
        response.setHeader(CorsConfigEnum.CORS_HEADERS.getKey(), CorsConfigEnum.CORS_HEADERS.getValue());
    }

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
