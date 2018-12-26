package com.letters7.wuchen.demo.generator.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:21
 * @use
 */
@Data
public class DataSourceConfigBO implements Serializable{
    private String dataSourceName;

    private String dataSourceType;

    private String dataSourceUrl;

    private String username;

    private String password;

}
