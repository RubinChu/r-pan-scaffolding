package com.rubin.rpan.common.config;

import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.util.FileUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件基础路径配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "rPanConfig")
@ConfigurationProperties(prefix = "rpan")
public class RPanConfig {

    /**
     * 分享访问路径前缀
     */
    private String sharePrefix = CommonConstant.DEFAULT_SHARE_PREFIX;

    public RPanConfig() {
    }

    public String getSharePrefix() {
        return sharePrefix;
    }

    public void setSharePrefix(String sharePrefix) {
        this.sharePrefix = sharePrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanConfig that = (RPanConfig) o;
        return Objects.equals(sharePrefix, that.sharePrefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharePrefix);
    }

    @Override
    public String toString() {
        return "RPanConfig{" +
                "sharePrefix='" + sharePrefix + '\'' +
                '}';
    }

}
