package com.letters7.wuchen.demo.generator.controller;

import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.MybatisGeneratorBridge;
import com.letters7.wuchen.sdk.ResponseMessage;
import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.IgnoredColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/28 10:13
 * @desc 用于处理生成
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {
    /**
     * 日志提供器
     */
    private final static Logger log = LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 生成相关配置文件，并弄成压缩包
     *
     * @param generatorConfig 配置信息
     * @return 压缩包
     */
    @PostMapping("/single")
    public ResponseMessage generator(@RequestBody GeneratorConfig generatorConfig) {

        // 获取数据源
        String host = UtilWeb.getCurrentIpBySpring();
        DatabaseConfig dataSource = dataSourceService.getDataSource(host);
        if (Objects.isNull(dataSource)) {
            return ResponseMessage.error("获取当前数据源配置信息异常，请重新配置后重试！");
        }
        File file = new File(UtilString.join(host, dataSource.getName()));
        if (!file.exists()) {
            file.mkdir();
            log.info("路径为:{}", file.getAbsolutePath());
        }

        if (!checkDirs(generatorConfig)) {
            log.info("创建文件夹！");
        }
        generatorConfig.setProjectFolder(file.getAbsolutePath());
        // 忽略的行数
        List<IgnoredColumn> ignoredColumns = Lists.newArrayList();
        // 重写的行数
        List<ColumnOverride> columnOverrides = Lists.newArrayList();

        MybatisGeneratorBridge bridge = new MybatisGeneratorBridge();
        bridge.setGeneratorConfig(generatorConfig);
        bridge.setDatabaseConfig(dataSource);
        bridge.setIgnoredColumns(ignoredColumns);
        bridge.setColumnOverrides(columnOverrides);
        try {
            bridge.generate();
        } catch (Exception e) {
            log.error("生成配置文件出现异常:{}", UtilException.getBootMessage(e));
        }
        return ResponseMessage.ok();
    }


    /**
     * 检查并创建不存在的文件夹
     *
     * @return
     */
    private boolean checkDirs(GeneratorConfig config) {
        List<String> dirs = new ArrayList<>();
        dirs.add(config.getProjectFolder());
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getModelPackageTargetFolder())));
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getDaoTargetFolder())));
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getMappingXMLTargetFolder())));
        boolean haveNotExistFolder = false;
        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.exists()) {
                haveNotExistFolder = true;
            }
        }
        if (haveNotExistFolder) {
            try {
                for (String dir : dirs) {
                    FileUtils.forceMkdir(new File(dir));
                }
                return true;
            } catch (Exception e) {
                log.warn("创建目录失败，请检查目录是否是文件而非目录");
            }
        }
        return true;
    }
}
