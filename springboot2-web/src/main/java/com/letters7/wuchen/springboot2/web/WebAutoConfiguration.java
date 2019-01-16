package com.letters7.wuchen.springboot2.web;

import com.letters7.wuchen.springboot2.Constants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuchen   2019/1/16 13:30
 * @version 0.1
 */
@Configuration
@ComponentScan(basePackages = {Constants.foundationPackagePrefix+"web"})
public class WebAutoConfiguration {
}
