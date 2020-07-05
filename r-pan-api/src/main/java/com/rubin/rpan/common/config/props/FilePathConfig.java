package com.rubin.rpan.common.config.props;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * File base path configuration class
 * Created by rubin on 2020/5/30.
 */

@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "file.root")
public class FilePathConfig {

    /**
     * Actual storage path prefix
     */
    private String path;

    /**
     * Static resource forwarding prefix
     */
    private String nginxPath;

    /**
     * Compressed file path prefix
     */
    private String zipPath;

}
