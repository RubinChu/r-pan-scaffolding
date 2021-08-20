package com.rubin.rpan.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传组件配置 使之上传的总容量最大为3G
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootConfiguration
public class FileUploadConfig {

    private static final Logger log = LoggerFactory.getLogger(FileUploadConfig.class);

    private static final Long MAX_SIZE = 3L * 1024L;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大3072M
        factory.setMaxFileSize(DataSize.of(MAX_SIZE, DataUnit.MEGABYTES));
        // 设置总上传数据总大小3072M
        factory.setMaxRequestSize(DataSize.of(MAX_SIZE, DataUnit.MEGABYTES));
        log.debug("RPan文件上传配置完毕！");
        return factory.createMultipartConfig();
    }

}
