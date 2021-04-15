package com.rubin.rpan.common.config.props;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件分享配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@Component(value = "fileShareConfig")
@ConfigurationProperties(prefix = "file.share")
public class FileShareConfig {

    /**
     * 分享链接前缀
     */
    private String prefix;

}
