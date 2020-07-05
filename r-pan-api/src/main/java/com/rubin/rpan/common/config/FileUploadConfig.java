package com.rubin.rpan.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * The file upload component configuration makes the total capacity of the upload maximum 3G
 * Created by rubin on 2019/9/7.
 */

@SpringBootConfiguration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(3072L, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(3072L, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
