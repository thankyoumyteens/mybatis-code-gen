package org.zsz.mybatiscodegen;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.service.GenService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MybatisCodeGenApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void genWithCode() throws Exception {

        DbUsers users = jdbcTemplate.queryForObject("select * from db_users where users_id=?",
                BeanPropertyRowMapper.newInstance(DbUsers.class), 3);
        if (users == null) return;
        System.out.println(users);
        String javaPath = "src/main/java";
        String xmlPath = "src/main/resources";
        new GenService(true)
                .setConnection(users)
                .setTypeResolver()
                .setEntityConfig("org.zsz.gen.entity", javaPath)
                .setDaoConfig("org.zsz.gen.dao", javaPath)
                .setMapperConfig("mapper", xmlPath)
                .addTable("db_users")
                .generateMapper();
    }

    @Test
    void genWithXml() throws Exception {
        List<String> warnings = new ArrayList<>();
        // 如果已经存在生成过的文件是否进行覆盖
        boolean overwrite = true;
        String fileName = "generator-configuration.xml";
        ClassPathResource rs = new ClassPathResource(fileName);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(rs.getInputStream());
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
        generator.generate(null);
        // 输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
        System.out.println();
    }

}
