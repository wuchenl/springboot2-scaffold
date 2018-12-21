package com.letters7.wuchen.springboot2.cache;

import com.google.common.collect.Maps;
import com.letters7.wuchen.springboot2.Constants;
import com.letters7.wuchen.springboot2.cache.support.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/20 17:08
 * @use
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheConfig.class)
@ComponentScan(Constants.foundationPackagePrefix + "cache")
public class RedisAutoConfiguration extends CachingConfigurerSupport {
    @Bean
    @ConditionalOnProperty(prefix = "spring.cache", name = "redis", matchIfMissing = true)
    public CacheManager caffeineCacheManager(CacheConfig cacheConfig, RedisConnectionFactory redisConnectionFactory) {

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        FastJsonRedisSerializer redisSerializer = new FastJsonRedisSerializer();

        Map<String, Long> cacheNames = cacheConfig.getCacheNames();
        Map<String, RedisCacheConfiguration> cacheConfigs = Maps.newConcurrentMap();
        // 优化了Map的迭代。直接整体迭代
        for (Map.Entry entry : cacheNames.entrySet()) {
            String cacheName = String.valueOf(entry.getKey());
            Long outTime = Long.valueOf(String.valueOf(entry.getValue()));
            RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(outTime))
                    .disableCachingNullValues()
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                    .prefixKeysWith(cacheName);
            cacheConfigs.put(cacheName, configuration);
        }
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        return new RedisCacheManager(redisCacheWriter, defaultConfig, cacheConfigs);
    }
}
