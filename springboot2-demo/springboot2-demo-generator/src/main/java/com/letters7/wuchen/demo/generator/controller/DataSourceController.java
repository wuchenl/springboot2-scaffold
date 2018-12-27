package com.letters7.wuchen.demo.generator.controller;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:15
 * @use
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @GetMapping("/all")
    public String getAllDataSource(){
        String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
        return null;
    }


    @GetMapping("/host")
    public String getHostDataSource(){
        String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
        DatabaseConfig dataSource = dataSourceService.getDataSource(currentIpBySpring);
        if (Objects.nonNull(dataSource)){
            return JSON.toJSONString(dataSource);
        }
        return "false";
    }

    @PostMapping("/host")
    public String addHostDataSource(@RequestBody DatabaseConfig databaseConfig){
        if (Objects.nonNull(databaseConfig)){
            String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
             dataSourceService.saveDataSource(currentIpBySpring,databaseConfig);
             return "true";
        }
        return "false";
    }
}
