package com.letters7.wuchen.demo.generator.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 14:16
 * @desc
 */
@Data
@Builder
@Accessors(chain = true)
public class ModelFiledBO {
    /**
     * 是否选中
     */
    private Boolean checked;
    /**
     * 表的列名
     */
    private String columnName;
    /**
     * model生成后的 字段类型
     */
    private String javaType;
    /**
     * 列的类型
     */
    private String jdbcType;
    /**
     * model中的属性名称
     */
    private String propertyName;
    /**
     * 处理的转换器类型
     */
    private String typeHandle;
}
