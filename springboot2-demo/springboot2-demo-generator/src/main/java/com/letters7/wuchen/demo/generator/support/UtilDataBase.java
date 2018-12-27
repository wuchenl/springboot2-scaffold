package com.letters7.wuchen.demo.generator.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.letters7.wuchen.demo.generator.model.DBType;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;
import com.letters7.wuchen.springboot2.utils.exception.ExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 10:52
 * @desc 数据库连接工具
 */
public class UtilDataBase {
    private final static Logger log= LoggerFactory.getLogger(UtilDataBase.class);

    /**
     * 数据库连接超时时间
     */
    private static final int DB_CONNECTION_TIMEOUTS_SECONDS = 30;

    /**
     * 根据数据类型，加载对应驱动
     */
    private static Map<DBType, Driver> drivers = Maps.newConcurrentMap();
    /**
     * 替换输入值进数据库Url
     * @param dbConfig 配置信息
     * @return 构造好的url
     */
    public static String getConnectionUrlWithSchema(DatabaseConfig dbConfig) {
        DBType dbType = DBType.valueOf(dbConfig.getDbType());
        String connectionUrl = String.format(dbType.getDbUrl(), dbConfig.getHost(), dbConfig.getPort(), dbConfig.getSchema(), dbConfig.getEncoding());
        log.info("完整的数据库请求连接信息:{}", connectionUrl);
        return connectionUrl;
    }


    /**
     * 创建数据库连接
     * @param config 数据库配置信息
     * @return 连接信息
     */
    public static Connection getConnection(DatabaseConfig config) {
        // 加载驱动
        DBType dbType = DBType.valueOf(config.getDbType());
        if (drivers.get(dbType) == null){
            loadDbDriver(dbType);
        }

        // 构造好URL
        String url = getConnectionUrlWithSchema(config);
        // 配置
        Properties props = new Properties();
        props.setProperty(GeneratorConst.DATABASE_USER, config.getUsername());
        props.setProperty(GeneratorConst.DATABASE_PASSWORD, config.getPassword());

        DriverManager.setLoginTimeout(DB_CONNECTION_TIMEOUTS_SECONDS);
        Connection connection = null;
        try {
            connection = drivers.get(dbType).connect(url, props);
        } catch (SQLException e) {
            String errorMsg= ExceptionHelper.getBootMessage(e);
            log.error("数据库连接失败:{}",errorMsg);
        }
        return connection;
    }

    /**
     * 加载数据库驱动
     * @param dbType 数据库类型
     */
    private static void loadDbDriver(DBType dbType){
        try {
            Class clazz = Class.forName(dbType.getDbClass());
            Driver driver = (Driver) clazz.newInstance();
            log.info("加载驱动成功: {}", driver);
            drivers.put(dbType, driver);
        } catch (IllegalAccessException e) {
            String errorMsg= ExceptionHelper.getBootMessage(e);
            log.error("转换驱动为Driver失败:{}",errorMsg);
        } catch (InstantiationException e) {
            String errorMsg= ExceptionHelper.getBootMessage(e);
            log.warn("实例化驱动失败:{}",errorMsg);
        } catch (ClassNotFoundException e) {
            String errorMsg= ExceptionHelper.getBootMessage(e);
            log.warn("加载驱动失败:{}，找不到对应驱动",dbType.getDbClass());
        }
    }

    /**
     * 获取某个连接的表信息
     * @param config 配置信息
     * @return 对应的表信息
     * @throws SQLException 对应的异常信息
     */
    public static List<String> getTableNames(DatabaseConfig config) throws SQLException {
        String url = getConnectionUrlWithSchema(config);
        log.info("getTableNames, connection url: {}", url);
        Connection connection=getConnection(config);
        List<String> tables = Lists.newArrayList();
        if (Objects.isNull(connection)) {
            return tables;
        }
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet;
            if (DBType.valueOf(config.getDbType())==DBType.Oracle){
                resultSet = databaseMetaData.getTables(null, config.getUsername().toUpperCase(), null, new String[] {GeneratorConst.DATABASE_TABLE,GeneratorConst.DATABASE_VIEW});
            }else if (DBType.valueOf(config.getDbType())==DBType.MySQL||DBType.valueOf(config.getDbType())==DBType.MySQL_8||DBType.valueOf(config.getDbType())==DBType.PostgreSQL){
                resultSet = databaseMetaData.getTables(config.getSchema(), null, GeneratorConst.DATABASE_ALL, new String[] {GeneratorConst.DATABASE_TABLE,GeneratorConst.DATABASE_VIEW});
            }else {
                return tables;
            }
            // 获取到的表名
            while (resultSet.next()) {
                tables.add(resultSet.getString(3));
            }
            return tables;
        }finally {
            connection.close();
        }
    }
//        try {
//            List<String> tables = new ArrayList<>();
//            DatabaseMetaData md = connection.getMetaData();
//            ResultSet rs;
//            if (DBType.valueOf(config.getDbType()) == DBType.SQL_Server) {
//                String sql = "select name from sysobjects  where xtype='u' or xtype='v' order by name";
//                rs = connection.createStatement().executeQuery(sql);
//                while (rs.next()) {
//                    tables.add(rs.getString("name"));
//                }
//            } else if (DBType.valueOf(config.getDbType()) == DBType.Oracle){
//                rs = md.getTables(null, config.getUsername().toUpperCase(), null, new String[] {"TABLE", "VIEW"});
//            } else if (DBType.valueOf(config.getDbType())==DBType.Sqlite){
//                String sql = "Select name from sqlite_master;";
//                rs = connection.createStatement().executeQuery(sql);
//                while (rs.next()) {
//                    tables.add(rs.getString("name"));
//                }
//            }
//            else {
//                rs = md.getTables(config.getSchema(), null, "%", new String[] {"TABLE", "VIEW"});			//针对 postgresql 的左侧数据表显示
//            }
//            while (rs.next()) {
//                tables.add(rs.getString(3));
//            }
//            return tables;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }


    /**
     * 获取某个表的字段信息
     * @param dbConfig 数据库配置信息
     * @param tableName 表名
     * @return 对应表的字段信息
     * @throws SQLException  SQL异常
     */
    public static List<ModelFiledBO> getTableColumns(DatabaseConfig dbConfig, String tableName) throws SQLException {
        Connection conn = getConnection(dbConfig);
        List<ModelFiledBO> columns = Lists.newArrayList();
        if (Objects.isNull(conn)){
            return columns;
        }
        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns(dbConfig.getSchema(), null, tableName, null);
            while (rs.next()) {
                ModelFiledBO modelFiledBO =ModelFiledBO.builder().build();
                String columnName = rs.getString(GeneratorConst.DATABASE_COLUMN_NAME);
                modelFiledBO.setColumnName(columnName);
                modelFiledBO.setJdbcType(rs.getString(GeneratorConst.DATABASE_TYPE_NAME));
                columns.add(modelFiledBO);
            }
            return columns;
        } finally {
            conn.close();
        }
    }
}
