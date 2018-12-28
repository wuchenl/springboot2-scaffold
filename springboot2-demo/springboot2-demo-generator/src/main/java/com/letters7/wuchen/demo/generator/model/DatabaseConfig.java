package com.letters7.wuchen.demo.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * @author wuchenl
 * @date 2018-12-27 19:03:13
 * @desc 数据源配置相关BO
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfig {

    @NotEmpty(message = "必须选择数据源类型")
    private String dbType;
    @NotEmpty(message = "必须填写该数据源配置名称")
    private String name;
    @NotEmpty(message = "必须填写该数据源地址")
    private String host;
    @NotEmpty(message = "必须填写该数据源端口")
    private String port;
    @NotEmpty(message = "必须填写该数据源数据库名")
    private String schema;
    @NotEmpty(message = "必须填写该数据源登录用户名")
    private String username;
    /**
     * 密码 非必选
     */
    private String password;
    /**
     * 编码，默认utf8
     */
    private String encoding;

}
