package com.rubin.rpan.cache;

/**
 * 缓存顶级接口
 * 外部扩展缓存可以继承该接口并配置rpan.cache.type=实现类的全限定类名 来自定义扩展
 */
public interface Cache {

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    void put(Object key, Object value, long expire);

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    void setExpire(Object key, long expire);

    /**
     * 清除一个key
     *
     * @param key
     */
    void delete(Object key);

}
