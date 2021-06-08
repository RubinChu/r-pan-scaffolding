package com.rubin.rpan.common.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件基础路径配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "filePathConfig")
@ConfigurationProperties(prefix = "file.root")
public class FilePathConfig {

    /**
     * 实际存放路径前缀
     */
    private String path;

    public FilePathConfig() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePathConfig that = (FilePathConfig) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "FilePathConfig{" +
                "path='" + path + '\'' +
                '}';
    }

}
