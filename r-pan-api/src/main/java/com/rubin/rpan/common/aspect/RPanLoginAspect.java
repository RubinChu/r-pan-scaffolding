package com.rubin.rpan.common.aspect;

import com.rubin.rpan.common.annotation.NeedLogin;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.response.R;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.util.JwtUtil;
import com.rubin.rpan.common.util.RedisUtil;
import com.rubin.rpan.common.util.UserIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 请求登录验证
 * Created by RubinChu on 2021/1/24 21:34
 */
@Aspect
@Component
@Slf4j
public class RPanLoginAspect {

    /**
     * 登录认证参数名称
     */
    private static final String LOGIN_AUTHENTICATION_PARAM_NAME = "token";

    /**
     * 请求头token的key
     */
    private final static String TOKEN_KEY = "Authorization";

    /**
     * 切点入口
     */
    private final String POINT_CUT = "execution(* com.rubin.rpan.modules.*.controller..*(..))";

    @Autowired
    @Qualifier(value = "redisUtil")
    private RedisUtil redisUtil;

    /**
     * 切点
     */
    @Pointcut(value = POINT_CUT)
    public void loginAuth() {
    }

    @Around("loginAuth()")
    public Object loginAuth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (checkNeedLogin(proceedingJoinPoint)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String uri = request.getRequestURI();
            log.debug("成功拦截到请求,uri为:{}", uri);
            if (!checkAndSaveUserId(request)) {
                log.warn("成功拦截到请求,uri为:{}, 检测到用户未登录,将跳转至登录页面", uri);
                return R.fail(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
            }
            log.debug("成功拦截到请求,uri为:{}, 请求通过", uri);
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * 检查并保存登录用户的ID
     * 此处会实现单设备登录功能 所以本套代码未考虑并发
     *
     * @param request
     * @return
     */
    private boolean checkAndSaveUserId(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(LOGIN_AUTHENTICATION_PARAM_NAME);
        }
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        String userId = JwtUtil.analyzeToken(token, CommonConstant.LOGIN_USER_ID);
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        String redisValue = redisUtil.getString(CommonConstant.USER_LOGIN_PREFIX + userId);
        if (StringUtils.isEmpty(redisValue)) {
            return false;
        }
        if (StringUtils.equals(redisValue, token)) {
            saveUserId(userId);
            return true;
        }
        return false;
    }

    /**
     * 保存用户ID到对应线程上
     *
     * @param userId
     */
    private void saveUserId(String userId) {
        UserIdUtil.set(userId);
    }

    /**
     * 校验是否需要登录
     *
     * @param proceedingJoinPoint
     * @return
     */
    private boolean checkNeedLogin(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        return targetMethod.isAnnotationPresent(NeedLogin.class);
    }

}
