package com.letters7.wuchen.demo.generator.controller;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.sdk.ResponseMessage;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:15
 * @desc 基于数据源的相关操作Controller
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 获取当前保存好的数据源
     * @return
     */
    @GetMapping("/host")
    public ResponseMessage getHostDataSource(){
        String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
        DatabaseConfig dataSource = dataSourceService.getDataSource(currentIpBySpring);
        if (Objects.nonNull(dataSource)){
            return ResponseMessage.ok(dataSource);
        }
        return ResponseMessage.error("");
    }

    /**
     * 保存填写好的数据源
     * @param databaseConfig
     * @return
     */
    @PostMapping("/host")
    public ResponseMessage addHostDataSource(@RequestBody DatabaseConfig databaseConfig){
        if (Objects.nonNull(databaseConfig)){
            String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
             dataSourceService.saveDataSource(currentIpBySpring,databaseConfig);
             return ResponseMessage.ok("");
        }
        return ResponseMessage.error("");
    }
}
