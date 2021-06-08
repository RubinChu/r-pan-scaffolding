package com.rubin.rpan.common.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.rubin.rpan.common.annotation.LogIgnore;
import com.rubin.rpan.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 请求日志打印
 * Created by RubinChu on 2021/1/24 21:34
 */
@Aspect
@Component
public class RPanRequestLogAspect {

    private static final Logger log = LoggerFactory.getLogger(RPanRequestLogAspect.class);

    /**
     * 切点入口
     */
    private final String POINT_CUT = "execution(* com.rubin.rpan.modules.*.controller..*(..))";

    /**
     * 切点
     */
    @Pointcut(value = POINT_CUT)
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 这一步获取到的方法有可能是代理方法也有可能是真实方法
        Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 判断代理对象本身是否是连接点所在的目标对象，不是的话就要通过反射重新获取真实方法
        if (joinPoint.getThis().getClass() != joinPoint.getTarget().getClass()) {
            m = ReflectUtil.getMethod(joinPoint.getTarget().getClass(), m.getName(), m.getParameterTypes());
        }
        if (m.isAnnotationPresent(LogIgnore.class)) {
            return;
        }
        // 通过真实方法获取该方法的参数名称
        LocalVariableTableParameterNameDiscoverer paramNames = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = paramNames.getParameterNames(m);
        // 获取连接点方法运行时的入参列表
        Object[] args = joinPoint.getArgs();
        // 将参数名称与入参值一一对应起来
        Map<String, Object> params = Maps.newHashMap();
        //自己写的一个判空类方法
        if (parameterNames != null && parameterNames.length > CommonConstant.ZERO_INT) {
            for (int i = CommonConstant.ZERO_INT; i < parameterNames.length; i++) {
                // 这里加一个判断，如果使用requestParam接受参数，加了require=false，这里会存现不存在的现象
                if (StringUtils.isBlank(parameterNames[i])) {
                    continue;
                }
                // 通过所在类转换，获取值，包含各种封装类都可以
                ObjectMapper objectMapper = new ObjectMapper();
                params.put(parameterNames[i], JSON.toJSON(objectMapper.convertValue(args[i], args[i].getClass())));
            }
        }
        log.debug("---------------------------request start---------------------------");
        log.debug("URL : " + request.getRequestURL().toString());
        log.debug("HTTP_METHOD : " + request.getMethod());
        log.debug("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + CommonConstant.POINT_STR + joinPoint.getSignature().getName());
        // 这里经过处理，就可以获得参数名字与值一一对应
        log.debug("BODY : " + params);
        // 这个就是纯粹拿到参数，值需要自己匹配
        log.debug("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        log.debug("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        log.debug("---------------------------request end---------------------------");
    }

    @AfterReturning(value = "log()", returning = "rtv")
    public void after(JoinPoint joinPoint, Object rtv) {
        log.debug("---------------------------response end---------------------------");
        log.debug("responseBody:" + JSON.toJSONString(rtv, SerializerFeature.WriteMapNullValue));
        log.debug("---------------------------response end---------------------------");
    }

}
