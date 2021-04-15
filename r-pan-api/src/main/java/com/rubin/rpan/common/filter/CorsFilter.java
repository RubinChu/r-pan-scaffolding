package com.rubin.rpan.common.filter;

import com.rubin.rpan.common.util.HttpUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域设置
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@WebFilter(filterName = "rPanCorsFilter")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpUtil.addCorsResponseHeader(response);
        chain.doFilter(req, res);
    }

}
