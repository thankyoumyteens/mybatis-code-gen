package org.zsz.mybatiscodegen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GenParam;
import org.zsz.mybatiscodegen.entity.Result;
import org.zsz.mybatiscodegen.service.MainService;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/main")
public class MainController {

    @Autowired
    MainService mainService;

    @RequestMapping("/index")
    public String index(){
        return "index" ;
    }

    @GetMapping
    @ResponseBody
    @RequestMapping("/init")
    public Result<String> init() {
        mainService.init();
        return Result.success();
    }

    @GetMapping
    @ResponseBody
    @RequestMapping("/list")
    public Result<List<DbUsers>> dbList() {
        List<DbUsers> dbUsersList = mainService.dbList();
        return Result.success(dbUsersList);
    }

    @PostMapping
    @ResponseBody
    @RequestMapping("/add")
    public Result<String> add(@RequestBody DbUsers dbUsers) {
        mainService.add(dbUsers);
        return Result.success();
    }

    @PostMapping
    @ResponseBody
    @RequestMapping("/gen")
    public Result<String> genMybatisCode(@RequestBody GenParam param) throws Exception {
        mainService.genMybatisCode(param);
        return Result.success();
    }
}
