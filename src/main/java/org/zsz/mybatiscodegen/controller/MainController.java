package org.zsz.mybatiscodegen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GeneratorProperty;
import org.zsz.mybatiscodegen.service.DbService;
import org.zsz.mybatiscodegen.service.GenService;

import java.util.List;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private DbService dbService;

    @GetMapping
    @RequestMapping("/init")
    public String init() {
        dbService.initUserDb();
        return "1";
    }

    @GetMapping
    @RequestMapping("/list")
    public List<DbUsers> dbList() {
        return dbService.selectList();
    }

    @PostMapping
    @RequestMapping("/add")
    public String add(@RequestBody DbUsers dbUsers) {
        dbService.insert(dbUsers);
        return "1";
    }

    @GetMapping
    @RequestMapping("/gen")
    public String genMybatisCode(Integer id, String tableName, Boolean useLombok, Boolean useComment) throws Exception {
        DbUsers users = dbService.selectById(id);
        String javaPath = "src/main/java";
        String xmlPath = "src/main/resources";
        GeneratorProperty[] commentProperties = {
                new GeneratorProperty("author", "z"),
                new GeneratorProperty("dateFormat", "yyyy/MM/dd HH:mm")
        };
        String entityPackage = "org.zsz.gen.entity";
        String daoPackage = "org.zsz.gen.dao";
        String mapperDir = "mapper";
        GenService genService = new GenService(true)
                .setConnection(users)
                .setTypeResolver("org.zsz.mybatiscodegen.config.NumberToIntJavaTypeResolver")
                .setEntityConfig(entityPackage, javaPath)
                .setDaoConfig(daoPackage, javaPath)
                .setMapperConfig(mapperDir, xmlPath)
                .addTable(tableName, "id", false);
        if (useLombok) {
            genService.addPlugin("org.zsz.mybatiscodegen.plugin.LombokPlugin");
        }
        if (useComment) {
            genService.setComment("org.zsz.mybatiscodegen.config.CustomCommentGenerator", commentProperties);
        }
        genService.generateMapper();
        return "1";
    }
}
