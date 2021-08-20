package com.rubin.rpan.storage.local.config;

import com.rubin.rpan.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件基础路径配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "localStorageConfig")
@ConfigurationProperties(prefix = "rpan.storage.local")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor")
public class LocalStorageConfig {

    private static final Integer SEVEN_INT = 7;

    /**
     * 实际存放路径前缀
     */
    private String rootFilePath = FileUtil.generateDefaultRootFolderPath();

    /**
     * 临时文件存放路径前缀
     */
    private String tempPath = FileUtil.generateTempFolderPath(rootFilePath);

    /**
     * 过期文件删除间隔时长（天，即删除最后修改时间在当前时间前几天的过期临时文件）
     */
    private Integer intervalDaysForClearingExpiredTempFiles = SEVEN_INT;

    public LocalStorageConfig() {
    }

    public String getRootFilePath() {
        return rootFilePath;
    }

    public void setRootFilePath(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public Integer getIntervalDaysForClearingExpiredTempFiles() {
        return intervalDaysForClearingExpiredTempFiles;
    }

    public void setIntervalDaysForClearingExpiredTempFiles(Integer intervalDaysForClearingExpiredTempFiles) {
        this.intervalDaysForClearingExpiredTempFiles = intervalDaysForClearingExpiredTempFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalStorageConfig that = (LocalStorageConfig) o;
        return Objects.equals(rootFilePath, that.rootFilePath) &&
                Objects.equals(tempPath, that.tempPath) &&
                Objects.equals(intervalDaysForClearingExpiredTempFiles, that.intervalDaysForClearingExpiredTempFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootFilePath, tempPath, intervalDaysForClearingExpiredTempFiles);
    }

    @Override
    public String toString() {
        return "LocalStorageConfig{" +
                "rootFilePath='" + rootFilePath + '\'' +
                ", tempPath='" + tempPath + '\'' +
                ", intervalDaysForClearingExpiredTempFiles=" + intervalDaysForClearingExpiredTempFiles +
                '}';
    }

}
