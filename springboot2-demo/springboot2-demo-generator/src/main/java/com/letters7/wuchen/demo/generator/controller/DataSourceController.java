package com.letters7.wuchen.demo.generator.controller;

import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:15
 * @use
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {


    @GetMapping("/all")
    public String getAllDataSource(){
        String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
        return null;
    }
}
