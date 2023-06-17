package org.zsz.mybatiscodegen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GenParam;
import org.zsz.mybatiscodegen.entity.GeneratorProperty;
import org.zsz.mybatiscodegen.service.DbService;
import org.zsz.mybatiscodegen.service.GenService;
import org.zsz.mybatiscodegen.service.MainService;

import java.util.List;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    MainService mainService;

    @GetMapping
    @RequestMapping("/init")
    public String init() {
        mainService.init();
        return "1";
    }

    @GetMapping
    @RequestMapping("/list")
    public List<DbUsers> dbList() {
        return mainService.dbList();
    }

    @PostMapping
    @RequestMapping("/add")
    public String add(@RequestBody DbUsers dbUsers) {
        mainService.add(dbUsers);
        return "1";
    }

    @PostMapping
    @RequestMapping("/gen")
    public String genMybatisCode(@RequestBody GenParam param) throws Exception {
        mainService.genMybatisCode(param);
        return "1";
    }
}
