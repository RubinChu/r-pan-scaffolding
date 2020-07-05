package com.rubin.rpan.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.common.util.RPanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * User login blocker
 * Created by rubin on 2019/9/20.
 */
@Slf4j
@Component
@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private RPanUtil rPanUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("successfully intercepted the request,uri为:{}", uri);
        if (Objects.isNull(rPanUtil.getUserId(request))) {
            log.warn("detected that the user is not logged in, will jump to the login page,uri is : {}", uri);
            resetDataResponse(response);
            return false;
        }
        log.info("request to pass,uri is : {}", uri);
        return true;
    }

    /**
     * Data interface Return logon required
     *
     * @param response
     * @throws IOException
     */
    private void resetDataResponse(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(Constants.UTF_8_STR);
        response.setContentType(Constants.APPLICATION_JSON_STR);
        ServerResponse serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(serverResponse));
        out.flush();
        out.close();
    }

}
