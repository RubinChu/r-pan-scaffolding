package com.rubin.rpan.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * Redis配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootConfiguration
@ConditionalOnProperty(prefix = "rpan.cache", name = "type", havingValue = "com.rubin.rpan.cache.redis.RedisCache")
@ConfigurationProperties(prefix = "rpan.cache.redis")
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    private static final String EMPTY_STR = "";
    private static final Integer DEFAULT_DATABASE = 0;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 6379;

    private Integer database = DEFAULT_DATABASE;
    private String host = DEFAULT_HOST;
    private Integer port = DEFAULT_PORT;
    private String password = EMPTY_STR;

    @Bean(value = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }

    @Bean(value = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        log.info("RPan Redis配置完毕！");
        return redisTemplate;
    }

    public RedisConfig() {
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisConfig that = (RedisConfig) o;
        return Objects.equals(database, that.database) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(database, host, port, password);
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "database=" + database +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                '}';
    }

}
