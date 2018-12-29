package com.letters7.wuchen.demo.generator.service;

import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;

import java.util.List;

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

    /**
     * 获取当前IP所对应的数据源的所有表
     * @param host IP信息
     * @return 对应的表名
     */
    List<String> getHostTables(String host);

    /**
     * 根据IP和表名获取对应表字段信息
     * @param host IP信息
     * @param tableName 对应的表名
     * @return 字段信息
     */
    List<ModelFiledBO> getTableColumnsByName(String host,String tableName);
}
