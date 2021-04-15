package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constant.CommonConstant;

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
        response.setHeader(CommonConstant.CorsConfigEnum.CORS_ORIGIN.getKey(), CommonConstant.CorsConfigEnum.CORS_ORIGIN.getValue());
        response.setHeader(CommonConstant.CorsConfigEnum.CORS_CREDENTIALS.getKey(), CommonConstant.CorsConfigEnum.CORS_CREDENTIALS.getValue());
        response.setHeader(CommonConstant.CorsConfigEnum.CORS_METHODS.getKey(), CommonConstant.CorsConfigEnum.CORS_METHODS.getValue());
        response.setHeader(CommonConstant.CorsConfigEnum.CORS_MAX_AGE.getKey(), CommonConstant.CorsConfigEnum.CORS_MAX_AGE.getValue());
        response.setHeader(CommonConstant.CorsConfigEnum.CORS_HEADERS.getKey(), CommonConstant.CorsConfigEnum.CORS_HEADERS.getValue());
    }

}
