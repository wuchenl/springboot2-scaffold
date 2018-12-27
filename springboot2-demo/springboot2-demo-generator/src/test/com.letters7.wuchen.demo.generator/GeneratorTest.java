package com.letters7.wuchen.demo.generator;

import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;
import com.letters7.wuchen.demo.generator.support.GeneratorConst;
import com.letters7.wuchen.demo.generator.support.UtilDataBase;
import com.letters7.wuchen.springboot2.utils.collection.UtilCollection;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 14:51
 * @desc
 */
public class GeneratorTest {
    /**
     * 测试数据库连接，此处使用了MySQL8
     * @throws SQLException
     */
    @Test
    public void testSqlConnect() {
        DatabaseConfig databaseConfig=DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");
        Connection connection = UtilDataBase.getConnection(databaseConfig);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testSqlGetTables() throws SQLException {
        DatabaseConfig databaseConfig=DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");
        List<String> tables=UtilDataBase.getTableNames(databaseConfig);
        System.out.println("获取到的表为："+tables);
        Assert.assertNotNull(tables);
    }

    @Test
    public void testSqlGetTableColmon() throws SQLException {
        DatabaseConfig databaseConfig=DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");
        List<String> tables=UtilDataBase.getTableNames(databaseConfig);
        if (UtilCollection.isNotEmpty(tables)){
            List<ModelFiledBO> tableColumns = UtilDataBase.getTableColumns(databaseConfig, tables.get(0));
            System.out.println("获取到的字段为："+tableColumns);
            Assert.assertNotNull(tableColumns);
        }
    }
}
