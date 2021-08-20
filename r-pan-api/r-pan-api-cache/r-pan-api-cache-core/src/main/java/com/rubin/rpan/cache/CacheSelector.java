package com.rubin.rpan.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 缓存选择器
 */
@Component(value = "cacheSelector")
public class CacheSelector implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Cache cache;

    @Value("${rpan.cache.type}")
    private Class<? extends Cache> cacheType;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (Objects.isNull(cacheType)) {
            throw new RuntimeException("the cache type can not be null!<rpan.cache.type>");
        }
        this.cache = this.applicationContext.getBean(cacheType);
    }

    /**
     * 选择Cache实现类
     *
     * @return
     */
    public Cache select() {
        return cache;
    }

}
