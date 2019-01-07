package com.letters7.wuchen.springboot2.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.letters7.wuchen.springboot2.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wuchen
 * @version 0.1
 *  2018/12/13 14:41
 *
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheConfig.class)
@ComponentScan(Constants.foundationPackagePrefix+"cache")
public class CaffeineAutoConfiguration extends CachingConfigurerSupport {
    @Bean
    @ConditionalOnClass(CaffeineCache.class)
    @ConditionalOnProperty(prefix = "spring.cache", name = "caffeine", matchIfMissing = true)
    public CacheManager caffeineCacheManager(CacheConfig cacheConfig) {

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        Map<String, Long> cacheNames = cacheConfig.getCacheNames();
        Iterator<String> cacheNameIter = cacheNames.keySet().iterator();
        while (cacheNameIter.hasNext()) {
            String cacheName = cacheNameIter.next();
            Long outTime = cacheNames.get(cacheName);
            // 讲cacheName,对应超时时间配置进去，单位为秒
            caches.add(new CaffeineCache(cacheName,Caffeine.newBuilder().recordStats().expireAfterWrite(outTime,TimeUnit.SECONDS).build()));
        }
        simpleCacheManager.setCaches(caches);
        return simpleCacheManager;
    }
}
