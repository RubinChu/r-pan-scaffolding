package com.rubin.rpan.interceptor;

import com.alibaba.fastjson.JSON;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.response.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 用户登录拦截器
 * Created by rubin on 2019/9/20.
 */

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("成功拦截到请求,uri为:{}", request.getRequestURI());

        HttpSession session = request.getSession();

        if (session.getAttribute(Constants.LOGIN_SESSION_KEY) == null) {
            logger.warn("成功拦截到请求,uri为:{}, 检测到用户未登录,将跳转至登录页面", request.getRequestURI());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            ServerResponse serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(serverResponse));
            out.flush();
            out.close();
            return false;
        }

        logger.info("成功拦截到请求,uri为:{}, 请求通过", request.getRequestURI());
        return true;
    }
}
