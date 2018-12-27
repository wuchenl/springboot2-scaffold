package com.letters7.wuchen.demo.generator.service;

import com.letters7.wuchen.demo.generator.model.DatabaseConfig;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 16:35
 * @desc
 */
public interface DataSourceService {
    /**
     * 保存数据库配置信息
     * @param host 访问ip
     * @param databaseConfig 数据库配置信息
     */
    void saveDataSource(String host,DatabaseConfig databaseConfig);

    /**
     * 获取当前配置信息
     * @param host ip
     * @return 配置信息
     */
    DatabaseConfig getDataSource(String host);
}
