package com.rubin.rpan.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 目前只用到String 只提供String的方法
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "redisUtil")
public class RedisUtil {

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 存储键值对
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void set(final String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 删除键值对
     *
     * @param keys
     * @return
     */
    public boolean del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                return redisTemplate.delete(keys[0]);
            } else {
                return redisTemplate.delete(Arrays.asList(keys)).intValue() == keys.length;
            }
        }
        return true;
    }

}
