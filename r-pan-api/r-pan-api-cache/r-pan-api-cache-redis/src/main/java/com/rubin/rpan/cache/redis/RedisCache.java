package com.rubin.rpan.cache.redis;

import com.rubin.rpan.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component(value = "redisCache")
@ConditionalOnProperty(prefix = "rpan.cache", name = "type", havingValue = "com.rubin.rpan.cache.redis.RedisCache")
public class RedisCache implements Cache {

    @Autowired
    @Qualifier(value = "redisUtil")
    private RedisUtil redisUtil;

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        return redisUtil.get(String.valueOf(key));
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        redisUtil.set(String.valueOf(key), value);
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
        redisUtil.set(String.valueOf(key), value, expire);
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    @Override
    public void setExpire(Object key, long expire) {
        redisUtil.expire(String.valueOf(key), expire);
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    @Override
    public void delete(Object key) {
        redisUtil.del(String.valueOf(key));
    }

}
