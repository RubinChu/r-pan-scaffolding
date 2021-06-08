package com.rubin.rpan.common.aspect;

import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.response.R;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.util.JwtUtil;
import com.rubin.rpan.common.util.ShareIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 请求分享码验证
 * Created by RubinChu on 2021/1/24 21:34
 */
@Aspect
@Component
public class RPanShareCodeAspect {

    private static final Logger log = LoggerFactory.getLogger(RPanShareCodeAspect.class);

    /**
     * 分享码token的key
     */
    private final static String SHARE_TOKEN_KEY = "Share-Token";

    /**
     * 分享认证参数名称
     */
    private static final String SHARE_AUTHENTICATION_PARAM_NAME = "shareToken";

    /**
     * 切点入口
     */
    private final String POINT_CUT = "@annotation(com.rubin.rpan.common.annotation.NeedShareCode)";

    /**
     * 切点
     */
    @Pointcut(value = POINT_CUT)
    public void shareCodeAuth() {
    }

    @Around("shareCodeAuth()")
    public Object loginAuth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uri = request.getRequestURI();
        log.debug("成功拦截到请求,uri为:{}", uri);
        if (!checkAndSaveShareId(request)) {
            log.warn("成功拦截到请求,uri为:{}, 检测到用户分享码失效,将跳转至分享码输入页面", uri);
            return R.fail(ResponseCode.NEED_SHARE_CODE.getCode(), ResponseCode.NEED_SHARE_CODE.getDesc());
        }
        log.debug("成功拦截到请求,uri为:{}, 请求通过", uri);
        return proceedingJoinPoint.proceed();
    }

    /**
     * 校验分享码token是否有效
     *
     * @param request
     * @return
     */
    private boolean checkAndSaveShareId(HttpServletRequest request) {
        String token = request.getHeader(SHARE_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(SHARE_AUTHENTICATION_PARAM_NAME);
        }
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        Object shareId = JwtUtil.analyzeToken(token, CommonConstant.SHARE_ID);
        if (Objects.isNull(shareId)) {
            return false;
        }
        saveShareId(shareId);
        return true;
    }

    /**
     * 保存分享id到对应的线程上
     *
     * @param shareId
     */
    private void saveShareId(Object shareId) {
        ShareIdUtil.set(Long.valueOf(String.valueOf(shareId)));
    }

}
