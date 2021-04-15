package com.rubin.rpan.common.config.props;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件基础路径配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@Component(value = "filePathConfig")
@ConfigurationProperties(prefix = "file.root")
public class FilePathConfig {

    /**
     * 实际存放路径前缀
     */
    private String path;

}
