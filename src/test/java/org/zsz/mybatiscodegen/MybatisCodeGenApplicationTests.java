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
import org.zsz.mybatiscodegen.entity.Users;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MybatisCodeGenApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void genWithCode() throws Exception {

        Users users = jdbcTemplate.queryForObject("select * from users where users_id=?",
                BeanPropertyRowMapper.newInstance(Users.class), 2);
        if (users == null) return;
        System.out.println(users);
        Configuration config = new Configuration();
        Context context = new Context(ModelType.FLAT);
        context.setId("myContext");
        // 生成的Java文件的编码
        context.addProperty("javaFileEncoding", "utf-8");
        context.setTargetRuntime("MyBatis3");

        // 创建Java类时对注释进行控制
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        // true: 不生成注释 false:自动生成注释
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        // jdbc连接
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        String connectionURL = String.format("jdbc:oracle:thin:@%s:%s:%s", users.getUsersUrl(), users.getPort(), users.getUsersDb());
        jdbcConnectionConfiguration.setConnectionURL(connectionURL);
        jdbcConnectionConfiguration.setPassword(users.getUsersPwd());
        jdbcConnectionConfiguration.setUserId(users.getUsersUid());
        jdbcConnectionConfiguration.setDriverClass(users.getUsersDriver());
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        // 类型转换
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.setConfigurationType("org.zsz.mybatiscodegen.NumberToIntJavaTypeResolver");
        javaTypeResolverConfiguration.addProperty("forceBigDecimals", "true");
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);

        // 生成实体类地址
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage("org.zsz.gen.entity");
        javaModelGeneratorConfiguration.setTargetProject("src/main/java");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        // 生成Mapper.xml文件
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage("mapper");
        sqlMapGeneratorConfiguration.setTargetProject("src/main/resources");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        // 生成 XxxMapper.java 接口
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage("org.zsz.gen.dao");
        javaClientGeneratorConfiguration.setTargetProject("src/main/java");
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        // 表
        TableConfiguration tc = new TableConfiguration(context);
        tc.setTableName("A_ASE_WORK_TASKS");
        tc.setDomainObjectName("WorkTasks");
        context.addTableConfiguration(tc);

        config.addContext(context);

        List<String> warnings = new ArrayList<>();
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
        generator.generate(null);
        // 输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
        System.out.println();
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
