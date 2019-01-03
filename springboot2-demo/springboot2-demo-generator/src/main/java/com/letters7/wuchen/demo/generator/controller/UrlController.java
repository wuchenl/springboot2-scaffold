package com.letters7.wuchen.demo.generator.controller;

import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DBType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author wuchenl
 * @date 2019/1/3.
 */
@Controller
public class UrlController {

    @GetMapping("/")
    public String index(Model model) {
        List<DBType> dbTypes = Lists.newArrayList();

        for (DBType dbType : DBType.values()) {
            dbTypes.add(dbType);
        }
        model.addAttribute("dbType", dbTypes);
        return "index";
    }
}
