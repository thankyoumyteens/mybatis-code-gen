package org.zsz.mybatiscodegen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GenParam;
import org.zsz.mybatiscodegen.entity.GeneratorProperty;

import java.io.File;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private DbService dbService;
    @Autowired
    private Environment environment;

    public void genMybatisCode(GenParam param) throws Exception {
        String distDir = environment.getProperty("walter.dist");
        DbUsers users = dbService.selectById(param.getId());
        String javaPath = distDir + "/java";
        String xmlPath = distDir + "/resources";
        File javaPathFile = new File(javaPath);
        if (!javaPathFile.exists()) {
            boolean mkdirs = javaPathFile.mkdirs();
            System.out.println("created javaPath dir:" + mkdirs);
        }
        File xmlPathFile = new File(xmlPath);
        if (!xmlPathFile.exists()) {
            boolean mkdirs = xmlPathFile.mkdirs();
            System.out.println("created xmlPath dir:" + mkdirs);
        }
        GeneratorProperty[] commentProperties = {
                new GeneratorProperty("author", "zsz"),
                new GeneratorProperty("dateFormat", "yyyy/MM/dd HH:mm")
        };
        String entityPackage = "entity";
        String daoPackage = "dao";
        String mapperDir = "mapper";
        GenService genService = new GenService(true)
                .setConnection(users)
                .setTypeResolver("org.zsz.mybatiscodegen.config.NumberToIntJavaTypeResolver")
                .setEntityConfig(entityPackage, javaPath)
                .setDaoConfig(daoPackage, javaPath)
                .setMapperConfig(mapperDir, xmlPath)
                .addPlugin("org.zsz.mybatiscodegen.plugin.IfWithStringNotEmptyTestPlugin")
                .addPlugin("org.zsz.mybatiscodegen.plugin.OverwriteMapperPlugin")
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

    public void del(DbUsers dbUsers) {
        dbService.del(dbUsers.getUsersId());
    }
}
