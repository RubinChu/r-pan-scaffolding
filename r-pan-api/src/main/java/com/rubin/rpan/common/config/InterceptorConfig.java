package com.rubin.rpan.common.config;

import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.interceptor.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor registration
 * Created by rubin on 2019/9/20.
 */

@SpringBootConfiguration
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(Constants.ALL_URI)
                .excludePathPatterns(Constants.WHITE_LIST);
    }
}
