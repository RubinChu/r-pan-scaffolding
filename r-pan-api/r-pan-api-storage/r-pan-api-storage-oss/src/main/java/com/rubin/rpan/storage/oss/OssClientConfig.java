package com.rubin.rpan.storage.oss;

import com.aliyun.oss.OSSClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.Objects;

@SpringBootConfiguration
@ConfigurationProperties(prefix = "rpan.storage.oss")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.oss.OssStorageProcessor")
public class OssClientConfig implements Serializable {
    private static final long serialVersionUID = -5370228155207535548L;

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    @Bean(value = "ossClient")
    public OSSClient ossClient() {
        if (StringUtils.isAnyBlank(endpoint, accessKeyId, accessKeySecret, bucketName)) {
            throw new RuntimeException("the oss config is missed!<rpan.storage.oss.endpoint><rpan.storage.oss.access-key-id><rpan.storage.oss.access-key-secret><rpan.storage.oss.bucket-name>");
        }
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public OssClientConfig() {
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OssClientConfig that = (OssClientConfig) o;
        return Objects.equals(endpoint, that.endpoint) &&
                Objects.equals(accessKeyId, that.accessKeyId) &&
                Objects.equals(accessKeySecret, that.accessKeySecret) &&
                Objects.equals(bucketName, that.bucketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, accessKeyId, accessKeySecret, bucketName);
    }

    @Override
    public String toString() {
        return "OssClientConfig{" +
                "endpoint='" + endpoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

}
