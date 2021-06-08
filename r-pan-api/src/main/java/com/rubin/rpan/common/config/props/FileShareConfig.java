package com.rubin.rpan.common.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件分享配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "fileShareConfig")
@ConfigurationProperties(prefix = "file.share")
public class FileShareConfig {

    /**
     * 分享链接前缀
     */
    private String prefix;

    public FileShareConfig() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileShareConfig that = (FileShareConfig) o;
        return Objects.equals(prefix, that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }

    @Override
    public String toString() {
        return "FileShareConfig{" +
                "prefix='" + prefix + '\'' +
                '}';
    }

}
