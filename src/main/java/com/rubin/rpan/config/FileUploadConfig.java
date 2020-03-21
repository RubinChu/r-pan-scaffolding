package com.rubin.rpan.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * Created by rubin on 2019/9/7.
 * 文件上传组件配置 使之上传的总容量最大为500M
 */

@SpringBootConfiguration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大3072M
        factory.setMaxFileSize(DataSize.of(3072L, DataUnit.MEGABYTES));
        // 设置总上传数据总大小3072M
        factory.setMaxRequestSize(DataSize.of(3072L, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
