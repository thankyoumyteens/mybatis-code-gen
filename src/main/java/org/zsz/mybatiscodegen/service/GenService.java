package org.zsz.mybatiscodegen.service;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.zsz.mybatiscodegen.constant.DbType;
import org.zsz.mybatiscodegen.entity.DbUsers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsz
 * @version 1.0
 * @create 2023/4/26 15:48
 * @description
 */
public class GenService {

    private Context context;

    /**
     * 初始化
     *
     * @param isXmlMode 用xml还是注解
     */
    public GenService(boolean isXmlMode) {
        context = new Context(ModelType.FLAT);
        context.setId("genServiceContext");
        context.addProperty("javaFileEncoding", "utf-8");
        context.setTargetRuntime(isXmlMode ? "MyBatis3" : "MyBatis3DynamicSql");
        // 创建Java类时对注释进行控制
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        // 不生成注释
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
    }

    /**
     * jdbc连接
     *
     * @param users jdbc连接信息
     */
    public GenService setConnection(DbUsers users) {
        JDBCConnectionConfiguration config = new JDBCConnectionConfiguration();
        String connectionURL = switch (users.getDbType()) {
            case DbType.ORACLE ->
                    String.format("jdbc:oracle:thin:@%s:%s:%s", users.getUsersUrl(), users.getPort(), users.getUsersDb());
            case DbType.MYSQL ->
                    String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&characterEncoding=utf8", users.getUsersUrl(), users.getPort(), users.getUsersDb());
            case DbType.MARIADB ->
                    String.format("jdbc:mariadb://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&characterEncoding=utf8", users.getUsersUrl(), users.getPort(), users.getUsersDb());
            default -> throw new RuntimeException("数据库类型未知");
        };
        config.setConnectionURL(connectionURL);
        config.setPassword(users.getUsersPwd());
        config.setUserId(users.getUsersUid());
        config.setDriverClass(users.getUsersDriver());
        context.setJdbcConnectionConfiguration(config);
        return this;
    }

    /**
     * 类型转换
     */
    public GenService setTypeResolver() {
        return setTypeResolver(null);
    }

    /**
     * 类型转换
     *
     * @param fullName TypeResolver全类名
     */
    public GenService setTypeResolver(String fullName) {
        JavaTypeResolverConfiguration config = new JavaTypeResolverConfiguration();
        if (fullName != null) {
            config.setConfigurationType(fullName);
        }
        config.addProperty("forceBigDecimals", "true");
        context.setJavaTypeResolverConfiguration(config);
        return this;
    }

    /**
     * 生成实体类配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setEntityConfig(String targetPackage, String targetProject) {
        JavaModelGeneratorConfiguration config = new JavaModelGeneratorConfiguration();
        config.setTargetPackage(targetPackage);
        config.setTargetProject(targetProject);
        config.addProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(config);
        return this;
    }

    /**
     * 生成Mapper.java接口配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setDaoConfig(String targetPackage, String targetProject) {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(targetPackage);
        javaClientGeneratorConfiguration.setTargetProject(targetProject);
        boolean isXmlMode = "MyBatis3".equals(context.getTargetRuntime());
        javaClientGeneratorConfiguration.setConfigurationType(isXmlMode ? "XMLMAPPER" : "ANNOTATEDMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        return this;
    }

    /**
     * 生成Mapper.xml文件配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setMapperConfig(String targetPackage, String targetProject) {
        SqlMapGeneratorConfiguration config = new SqlMapGeneratorConfiguration();
        config.setTargetPackage(targetPackage);
        config.setTargetProject(targetProject);
        context.setSqlMapGeneratorConfiguration(config);
        return this;
    }

    /**
     * 添加表
     *
     * @param tableName 表名
     */
    public GenService addTable(String tableName) {
        return addTable(null, tableName, null);
    }

    /**
     * 添加表
     *
     * @param dbName 数据库
     * @param tableName  表名
     * @param entityName 实体类名
     */
    public GenService addTable(String dbName, String tableName, String entityName) {
        TableConfiguration tc = new TableConfiguration(context);
        if (dbName != null) {
            tc.setSchema(dbName);
        }
        tc.setTableName(tableName);
        if (entityName != null) {
            tc.setDomainObjectName(entityName);
        }
        tc.setCountByExampleStatementEnabled(false);
        tc.setDeleteByExampleStatementEnabled(false);
        tc.setSelectByExampleStatementEnabled(false);
        tc.setUpdateByExampleStatementEnabled(false);
        tc.setSelectByExampleQueryId("false");
        context.addTableConfiguration(tc);
        return this;
    }

    public void generateMapper() throws InvalidConfigurationException, SQLException, IOException, InterruptedException {
        Configuration config = new Configuration();
        config.addContext(context);
        List<String> warnings = new ArrayList<>();
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
        generator.generate(null);
        // 输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
