package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constants.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis tools
 * Currently only String is used, only String methods are provided
 * Created by RubinChu on 2020/6/9 16:31
 */
@Component
@AllArgsConstructor
public class RedisUtil {

    private RedisTemplate redisTemplate;

    /**
     * store key-value pairs
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void setString(final String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * get value
     *
     * @param key
     * @return
     */
    public String getString(final String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return Constants.NULL_STR;
        }
        return String.valueOf(value);
    }

    /**
     * delete key-value pairs
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
