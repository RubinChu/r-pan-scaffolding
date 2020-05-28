package com.rubin.rpan.interceptor;

import com.rubin.rpan.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 用户登录拦截器
 * Created by rubin on 2019/9/20.
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("成功拦截到请求,uri为:{}", uri);
        if (Objects.isNull(request.getSession().getAttribute(Constants.LOGIN_SESSION_KEY))) {
            log.warn("成功拦截到请求,uri为:{}, 检测到用户未登录,将跳转至登录页面", uri);
            response.sendRedirect(Constants.LOGIN_PATH);
            return false;
        }
        log.info("成功拦截到请求,uri为:{}, 请求通过", request.getRequestURI());
        return true;
    }

}
