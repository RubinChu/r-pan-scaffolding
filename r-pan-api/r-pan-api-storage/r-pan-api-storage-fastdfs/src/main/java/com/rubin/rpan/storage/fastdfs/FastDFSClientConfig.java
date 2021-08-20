package com.rubin.rpan.storage.fastdfs;

import com.github.tobato.fastdfs.conn.ConnectionPoolConfig;
import com.github.tobato.fastdfs.conn.FdfsConnectionPool;
import com.github.tobato.fastdfs.conn.PooledConnectionFactory;
import com.github.tobato.fastdfs.conn.TrackerConnectionManager;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SpringBootConfiguration
@ComponentScan(value = {"com.github.tobato.fastdfs.service", "com.github.tobato.fastdfs.domain"})
// 解决jmx 重复注册bean
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConfigurationProperties(prefix = "rpan.storage.fdfs")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.fastdfs.FastDFSStorageProcessor")
public class FastDFSClientConfig implements Serializable {

    private static final long serialVersionUID = -7298184878964452046L;

    private Integer connectTimeout = 600;

    private List<String> trackerList = Lists.newArrayList();

    public FastDFSClientConfig() {
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public List<String> getTrackerList() {
        return trackerList;
    }

    public void setTrackerList(List<String> trackerList) {
        this.trackerList = trackerList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FastDFSClientConfig that = (FastDFSClientConfig) o;
        return Objects.equals(connectTimeout, that.connectTimeout) &&
                Objects.equals(trackerList, that.trackerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectTimeout, trackerList);
    }

    @Override
    public String toString() {
        return "FastDFSClientConfig{" +
                "connectTimeout=" + connectTimeout +
                ", trackerList=" + trackerList +
                '}';
    }

    @Bean("connectionPoolConfig")
    public ConnectionPoolConfig connectionPoolConfig() {
        ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
        return connectionPoolConfig;
    }

    @Bean("pooledConnectionFactory")
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectTimeout(connectTimeout);
        return pooledConnectionFactory;
    }

    @Bean(value = "fdfsConnectionPool")
    public FdfsConnectionPool fdfsConnectionPool(ConnectionPoolConfig connectionPoolConfig, PooledConnectionFactory pooledConnectionFactory) {
        FdfsConnectionPool fdfsConnectionPool = new FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig);
        return fdfsConnectionPool;
    }

    @Bean(value = "trackerConnectionManager")
    public TrackerConnectionManager trackerConnectionManager(FdfsConnectionPool fdfsConnectionPool) {
        TrackerConnectionManager trackerConnectionManager = new TrackerConnectionManager(fdfsConnectionPool);
        if (CollectionUtils.isEmpty(trackerList)) {
            throw new RuntimeException("the tracker list can not be enpty!<rpan.storage.fdfs.tracker-list>");
        }
        trackerConnectionManager.setTrackerList(trackerList);
        return trackerConnectionManager;
    }

}
