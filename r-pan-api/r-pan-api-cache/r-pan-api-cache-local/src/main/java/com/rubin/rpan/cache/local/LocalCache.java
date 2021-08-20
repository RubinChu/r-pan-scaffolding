package com.rubin.rpan.cache.local;

import com.rubin.rpan.cache.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认Cache实现
 * 使用jvm本地内存实现，不支持数据持久化，支持过期时间自动删除
 * 由于网盘业务对于同一个key不存在同步读写，所以并发问题未深入控制
 */
@Component(value = "localCache")
@ConditionalOnProperty(prefix = "rpan.cache", name = "type", havingValue = "com.rubin.rpan.cache.local.LocalCache")
public class LocalCache implements Cache, Serializable {

    private static final long serialVersionUID = 8696403508878817326L;

    /**
     * 缓存池
     */
    private Map<Object, Object> CACHE_POOL = new ConcurrentHashMap<>();

    /**
     * key的过期池
     */
    private Map<Object, Long> EXPIRE_POOL = new ConcurrentHashMap<>();

    /**
     * 永久有效标识
     */
    private final static Long FOREVER_SIGN = -1L;

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        Object value = CACHE_POOL.get(key);
        if (Objects.isNull(value)) {
            return value;
        }
        Long longTime = EXPIRE_POOL.get(key);
        if (!FOREVER_SIGN.equals(longTime)) {
            Date expireDate = new Date(EXPIRE_POOL.get(key));
            if (new Date().after(expireDate)) {
                delete(key);
                value = null;
            }
        }
        return value;
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        CACHE_POOL.put(key, value);
        EXPIRE_POOL.put(key, FOREVER_SIGN);
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
        CACHE_POOL.put(key, value);
        setExpire(key, expire);
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    @Override
    public void setExpire(Object key, long expire) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int) (expire/1000));
        EXPIRE_POOL.put(key, calendar.getTime().getTime());
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    @Override
    public void delete(Object key) {
        CACHE_POOL.remove(key);
        EXPIRE_POOL.remove(key);
    }

}
