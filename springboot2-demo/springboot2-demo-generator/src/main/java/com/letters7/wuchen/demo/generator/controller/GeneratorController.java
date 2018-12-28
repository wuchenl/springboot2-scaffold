package com.letters7.wuchen.demo.generator.controller;

import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/28 10:13
 * @desc 用于处理生成
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {


    @PostMapping
    public String generator(@RequestBody GeneratorConfig generatorConfig){

        return "";
    }
}
