package com.rubin.rpan.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.rubin.rpan.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: r-pan-api
 * Created by RubinChu on 2020/6/5 14:57
 */
@Slf4j
public class LocalCacheUtil {

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) {
                    return Constants.NULL_STR;
                }
            });

    /**
     * Save reset password token default expiration time 5 minutes
     *
     * @param key
     * @param value
     */
    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    /**
     * Get the reset password token
     *
     * @param key
     * @return
     */
    public static String getKey(String key) {
        String value;
        try {
            value = localCache.get(key);
            if (Objects.equals(Constants.NULL_STR, value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("localCache get error", e);
        }
        return null;
    }
}
