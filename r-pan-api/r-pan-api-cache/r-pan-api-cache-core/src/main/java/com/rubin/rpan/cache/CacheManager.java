package com.rubin.rpan.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 缓存管理器
 */
@Component(value = "cacheManager")
public class CacheManager implements Cache {

    @Autowired
    @Qualifier(value = "cacheSelector")
    private CacheSelector cacheSelector;

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        return cacheSelector.select().get(key);
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        cacheSelector.select().put(key, value);
    }

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    @Override
    public void put(Object key, Object value, long expire) {
        cacheSelector.select().put(key, value, expire);
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    @Override
    public void setExpire(Object key, long expire) {
        cacheSelector.select().setExpire(key, expire);
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    @Override
    public void delete(Object key) {
        cacheSelector.select().delete(key);
    }

}
