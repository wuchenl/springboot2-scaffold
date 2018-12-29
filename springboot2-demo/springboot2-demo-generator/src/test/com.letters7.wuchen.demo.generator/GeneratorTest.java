package com.letters7.wuchen.demo.generator;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
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
     *
     * @throws SQLException
     */
    @Test
    public void testSqlConnect() {
        DatabaseConfig databaseConfig = DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");

        Connection connection = UtilDataBase.getConnection(databaseConfig);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testSqlGetTables() throws SQLException {
        DatabaseConfig databaseConfig = DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");
        List<String> tables = UtilDataBase.getTableNames(databaseConfig);
        System.out.println("获取到的表为：" + tables);
        Assert.assertNotNull(tables);
    }

    @Test
    public void testSqlGetTableColmon() throws SQLException {
        DatabaseConfig databaseConfig = DatabaseConfig.builder().build();
        databaseConfig.setDbType("MySQL_8").setEncoding(GeneratorConst.ENCODE_UTF8).setHost("kn.letters7.com").setPort("33308")
                .setName("test").setUsername("wuchenl").setPassword("g20130817!").setSchema("ownKnowledge");
        List<String> tables = UtilDataBase.getTableNames(databaseConfig);
        if (UtilCollection.isNotEmpty(tables)) {
            List<ModelFiledBO> tableColumns = UtilDataBase.getTableColumns(databaseConfig, tables.get(0));
            System.out.println("获取到的字段为：" + tableColumns);
            Assert.assertNotNull(tableColumns);
        }
    }


    @Test
    public void testGeneratorBo() {
        GeneratorConfig generatorConfig = GeneratorConfig.builder().build();
        generatorConfig.setName("测试生成")
                .setProjectFolder("/")
                .setModelPackage("com.example.model")
                .setModelPackageTargetFolder("src/main/java")
                .setDaoPackage("com.example.dao")
                .setDaoTargetFolder("src/main/java")
                .setMapperName("")
                .setMappingXMLPackage("com.example.dao.mapper")
                .setMappingXMLTargetFolder("src/main/java")
                .setTableName("t_aqi_day")
                .setDomainObjectName("AqiDayBo")
                .setOverrideXML(true)
                .setNeedToStringHashcodeEquals(false)
                .setNeedForUpdate(false)
                .setAnnotation(false)
                .setAnnotationDAO(false)
                .setOffsetLimit(false)
                .setComment(false)
                .setUseActualColumnNames(false)
                .setUseExample(false)
                .setGenerateKeys("")
                .setEncoding("utf-8")
                .setUseDAOExtendStyle(false)
                .setUseSchemaPrefix(true)
                .setUseLombok(true)
                .setJsr310Support(false)
                .setUseTableNameAlias(false)
                .setDatasourceName("test");
        System.out.println(JSON.toJSONString(generatorConfig));

    }
}
