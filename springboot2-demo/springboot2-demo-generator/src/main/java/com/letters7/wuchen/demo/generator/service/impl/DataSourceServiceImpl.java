package com.letters7.wuchen.demo.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.GeneratorConst;
import com.letters7.wuchen.springboot2.cache.CacheManagerHolder;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 16:36
 * @desc 用于保存和获取数据库连接配置信息
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    /**
     * 日志提供器
     */
    private static final Logger log = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    /**
     * 保存数据库配置信息
     *
     * @param host           请求者ip信息
     * @param databaseConfig 数据库配置信息
     */
    @Override
    public void saveDataSource(String host, DatabaseConfig databaseConfig) {
        log.info("即将保存的数据库配置信息的key为:{}，配置信息为:{}", host, databaseConfig);
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(GeneratorConst.CACHE_DATASOURCE);
            if (Objects.nonNull(cache)) {
                cache.put(host, JSON.toJSONString(databaseConfig));
            }
        }
    }

    /**
     * 获取当前配置信息
     *
     * @param host ip
     * @return 配置信息
     */
    @Override
    public DatabaseConfig getDataSource(String host) {
        CacheManager manager = CacheManagerHolder.getManager();
        if (Objects.nonNull(manager)) {
            Cache cache = manager.getCache(GeneratorConst.CACHE_DATASOURCE);
            if (Objects.nonNull(cache)) {
                Cache.ValueWrapper valueWrapper = cache.get(host);
                if (Objects.nonNull(valueWrapper)){
                    String json=valueWrapper.get().toString();
                    if (UtilString.isNotEmpty(json)){
                        return JSON.parseObject(json,DatabaseConfig.class);
                    }
                }
            }
        }
        return null;
    }
}
