package com.letters7.wuchen.demo.generator.model;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:10
 * @use
 */
public enum DBType {
    MySQL("mysql5.x", "jdbc:mysql://%s:%s/%s?useUnicode=true&useSSL=false&characterEncoding=%s", "com.mysql.jdbc.Driver"),
    MySQL_8("mysql8", "jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useUnicode=true&useSSL=false&characterEncoding=%s", "com.mysql.cj.jdbc.Driver"),
    Oracle("oracle", "jdbc:oracle:thin:@//%s:%s/%s", "oracle.jdbc.OracleDriver"),
    PostgreSQL("postgresql", "jdbc:postgresql://%s:%s/%s", "org.postgresql.Driver");

    DBType(String dbName, String dbUrl, String dbClass) {
        this.dbName = dbName;
        this.dbUrl = dbUrl;
        this.dbClass = dbClass;
    }

    /**
     * 数据库类型
     */
    private String dbName;
    /**
     * 数据库连接字符串
     */
    private String dbUrl;
    /**
     * 数据库驱动类名
     */
    private String dbClass;

    public String getDbName() {
        return dbName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbClass() {
        return dbClass;
    }
}
