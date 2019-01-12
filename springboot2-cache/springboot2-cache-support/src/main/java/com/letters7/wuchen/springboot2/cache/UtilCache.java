package com.letters7.wuchen.springboot2.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * @author wuchenl
 * 2019/1/12.
 */
public class UtilCache {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(UtilCache.class);

    /**
     * 从缓存中加载数据
     *
     * @param cacheName 缓存名称
     * @param cacheKey  key
     * @return 对应数据
     */
    private String getDataFromCache(@NonNull String cacheName, @NonNull String cacheKey) {
        // 拿取缓存
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(cacheName);
            if (Objects.nonNull(cache)) {
                Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
                if (Objects.nonNull(valueWrapper)) {
                    Object object = valueWrapper.get();
                    if (Objects.nonNull(object)) {
                        return String.valueOf(object);
                    }
                }
            } else {
                logger.warn("从缓存【{}】读取数据异常，key：{}", cacheName, cacheKey);
            }
        } else {
            logger.warn("从缓存【{}】读取数据异常，key：{}", cacheName, cacheKey);
        }
        return null;
    }


    /**
     * 利用CacheHolder放置数据进缓存
     *
     * @param cacheName 缓存名称
     * @param cacheKey  key
     * @param value     JSON格式的值
     */
    public static boolean putDataToCache(@NonNull String cacheName, @NonNull String cacheKey, String value) {
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(cacheName);
            if (Objects.nonNull(cache)) {
                cache.put(cacheKey, value);
                return true;
            }
        }
        logger.warn("放置数据进缓存【{}】异常，key：{}，value:{}", cacheName, cacheKey, value);
        return false;
    }
}
