package com.letters7.wuchen.demo.redis.controller;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.redis.model.User;
import com.letters7.wuchen.springboot2.cache.CacheManagerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author wuchenl
 * @date 2018/12/20.
 */
@RestController
public class RedisRestController {
    private final static Logger log = LoggerFactory.getLogger(RedisRestController.class);
    /**
     * Cache的名称
     */
    private static final String CACHE_USER = "cacheUser";

    @GetMapping("/user")
    public String getUser() {
        String userInfo = getDataFromCache("test");
        log.info("从缓存中读取的数据为:{}", userInfo);
        return userInfo;
    }

    @PostMapping("/user")
    public String addUser() {
        User user = User.builder().build();
        user.setAddress("四川省成都");
        user.setSex("男");
        user.setUserName("测试人员");
        putDataToCache("test", JSON.toJSONString(user));
        return "ok";
    }

    /**
     * 将某些数据放入缓存
     *
     * @param name  缓存的key
     * @param value 值
     */
    private void putDataToCache(String name, String value) {
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(CACHE_USER);
            if (Objects.nonNull(cache)) {
                cache.put(name, value);
            }
        }
    }

    /**
     * 从缓存中获取某些数据
     *
     * @param name 数据key
     * @return
     */
    private String getDataFromCache(String name) {
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(CACHE_USER);
            if (Objects.nonNull(cache)) {
                Cache.ValueWrapper valueWrapper = cache.get(name);
                if (Objects.nonNull(valueWrapper)) {
                    return valueWrapper.get().toString();
                }
            }
        }
        return null;
    }
}
