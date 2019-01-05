package com.letters7.wuchen.demo.generator;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DBType;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 10:10
 * @desc
 */
public class TestClass {

    @Test
    public void testClassFind() throws ClassNotFoundException, IOException {

        String path = Class.forName(DBType.MySQL.getDbClass()).getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path);
        String libPath = URLDecoder.decode(file.getCanonicalPath(), Charset.forName("UTF-8").displayName());
        System.out.println(libPath);
    }


    @Test
    public void testNull() {
        String json="{\"annotation\":false,\"annotationDAO\":false,\"comment\":false,\"jsr310Support\":false,\"needForUpdate\":false,\"needToStringHashcodeEquals\":false,\"offsetLimit\":false,\"overrideXML\":false,\"useActualColumnNames\":false,\"useDAOExtendStyle\":false,\"useExample\":false,\"useSchemaPrefix\":false,\"useTableNameAlias\":false}\n";
        GeneratorConfig generatorConfig2 = GeneratorConfig.builder().build();
        GeneratorConfig generatorConfig=JSON.parseObject(json,GeneratorConfig.class);
        generatorConfig2.setProjectFolder(generatorConfig.getProjectFolder());
        System.out.println(generatorConfig2);
    }
}
