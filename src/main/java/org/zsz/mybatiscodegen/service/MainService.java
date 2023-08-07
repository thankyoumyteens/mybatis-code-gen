package org.zsz.mybatiscodegen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GenParam;
import org.zsz.mybatiscodegen.entity.GeneratorProperty;

import java.util.List;

@Service
public class MainService {

    @Autowired
    private DbService dbService;

    public void genMybatisCode(GenParam param) throws Exception {
        DbUsers users = dbService.selectById(param.getId());
        String javaPath = "src/main/java";
        String xmlPath = "src/main/resources";
        GeneratorProperty[] commentProperties = {
                new GeneratorProperty("author", "zsz"),
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
                .addPlugin("org.zsz.mybatiscodegen.plugin.IfWithStringNotEmptyTestPlugin")
                .addPlugin("org.zsz.mybatiscodegen.plugin.OverIsMergeablePlugin")
                .addTable(param.getTableName(), "id", false);
        if (param.getUseLombok()) {
            genService.addPlugin("org.zsz.mybatiscodegen.plugin.LombokPlugin");
        }
        if (param.getUseComment()) {
            genService.setComment("org.zsz.mybatiscodegen.config.CustomCommentGenerator", commentProperties);
        } else {
            genService.noComment();
        }
        genService.generateMapper();
    }

    public void add(DbUsers dbUsers) {
        dbService.insert(dbUsers);
    }

    public List<DbUsers> dbList() {
        return dbService.selectList();
    }

    public void init() {
        dbService.initUserDb();
    }
}
