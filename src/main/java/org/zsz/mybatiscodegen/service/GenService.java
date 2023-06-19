package org.zsz.mybatiscodegen.service;

import cn.hutool.core.util.StrUtil;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.zsz.mybatiscodegen.constant.DbType;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.GeneratorProperty;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsz
 * @version 1.0
 */
public class GenService {

    private final Context context;

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
    }

    /**
     * jdbc连接
     *
     * @param users jdbc连接信息
     */
    public GenService setConnection(DbUsers users, GeneratorProperty... properties) {
        JDBCConnectionConfiguration config = new JDBCConnectionConfiguration();
        String connectionURL;
        switch (users.getDbType()) {
            case DbType.ORACLE:
                connectionURL = String.format("jdbc:oracle:thin:@%s:%s:%s", users.getUsersUrl(), users.getUsersPort(), users.getUsersDb());
                // 解决获取不到字段注释的问题
                config.addProperty("remarksReporting", "true");
                break;
            case DbType.MYSQL:
                connectionURL = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&characterEncoding=utf8", users.getUsersUrl(), users.getUsersPort(), users.getUsersDb());
                // 解决获取不到字段注释的问题
                config.addProperty("useInformationSchema", "true");
                break;
            case DbType.MARIADB:
                connectionURL = String.format("jdbc:mariadb://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&characterEncoding=utf8", users.getUsersUrl(), users.getUsersPort(), users.getUsersDb());
                break;
            default:
                throw new RuntimeException("数据库类型未知");
        }
        config.setConnectionURL(connectionURL);
        config.setPassword(users.getUsersPwd());
        config.setUserId(users.getUsersUid());
        config.setDriverClass(users.getUsersDriver());
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setJdbcConnectionConfiguration(config);
        return this;
    }

    /**
     * 添加插件
     *
     * @param fullName 插件全类名
     */
    public GenService addPlugin(String fullName, GeneratorProperty... properties) {
        PluginConfiguration config = new PluginConfiguration();
        config.setConfigurationType(fullName);
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.addPluginConfiguration(config);
        return this;
    }

    /**
     * 不生成注释
     */
    public GenService noComment() {
        CommentGeneratorConfiguration config = new CommentGeneratorConfiguration();
        config.addProperty("suppressAllComments", "true");
        context.setCommentGeneratorConfiguration(config);
        return this;
    }

    /**
     * 注释
     *
     * @param fullName 自定义注释生成类全类名
     */
    public GenService setComment(String fullName, GeneratorProperty... properties) {
        CommentGeneratorConfiguration config = new CommentGeneratorConfiguration();
        config.setConfigurationType(fullName);
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setCommentGeneratorConfiguration(config);
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
    public GenService setTypeResolver(String fullName, GeneratorProperty... properties) {
        JavaTypeResolverConfiguration config = new JavaTypeResolverConfiguration();
        if (fullName != null) {
            config.setConfigurationType(fullName);
        }
        config.addProperty("forceBigDecimals", "true");
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setJavaTypeResolverConfiguration(config);
        return this;
    }

    /**
     * 生成实体类配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setEntityConfig(String targetPackage, String targetProject, GeneratorProperty... properties) {
        JavaModelGeneratorConfiguration config = new JavaModelGeneratorConfiguration();
        config.setTargetPackage(targetPackage);
        config.setTargetProject(targetProject);
        config.addProperty("trimStrings", "true");
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setJavaModelGeneratorConfiguration(config);
        return this;
    }

    /**
     * 生成Mapper.java接口配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setDaoConfig(String targetPackage, String targetProject, GeneratorProperty... properties) {
        JavaClientGeneratorConfiguration config = new JavaClientGeneratorConfiguration();
        config.setTargetPackage(targetPackage);
        config.setTargetProject(targetProject);
        boolean isXmlMode = "MyBatis3".equals(context.getTargetRuntime());
        config.setConfigurationType(isXmlMode ? "XMLMAPPER" : "ANNOTATEDMAPPER");
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setJavaClientGeneratorConfiguration(config);
        return this;
    }

    /**
     * 生成Mapper.xml文件配置
     *
     * @param targetPackage 包名
     * @param targetProject 文件路径
     */
    public GenService setMapperConfig(String targetPackage, String targetProject, GeneratorProperty... properties) {
        SqlMapGeneratorConfiguration config = new SqlMapGeneratorConfiguration();
        config.setTargetPackage(targetPackage);
        config.setTargetProject(targetProject);
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.setSqlMapGeneratorConfiguration(config);
        return this;
    }

    /**
     * 添加表
     *
     * @param tableName 表名
     */
    public GenService addTable(String tableName) {
        return addTable(null, tableName, null, null, false);
    }

    /**
     * 添加表
     *
     * @param tableName  表名
     * @param primaryKey 主键列名
     * @param isIdentity 主键是否自增
     */
    public GenService addTable(String tableName, String primaryKey, boolean isIdentity) {
        return addTable(null, tableName, null, primaryKey, isIdentity);
    }

    /**
     * 添加表
     *
     * @param dbName     数据库
     * @param tableName  表名
     * @param entityName 实体类名
     * @param primaryKey 主键列名
     * @param isIdentity 主键是否自增
     */
    public GenService addTable(String dbName, String tableName, String entityName, String primaryKey, boolean isIdentity, GeneratorProperty... properties) {
        TableConfiguration config = new TableConfiguration(context);
        if (dbName != null) {
            config.setSchema(dbName);
        }
        config.setTableName(tableName);
        if (entityName != null) {
            config.setDomainObjectName(entityName);
        }
        config.setCountByExampleStatementEnabled(false);
        config.setDeleteByExampleStatementEnabled(false);
        config.setSelectByExampleStatementEnabled(false);
        config.setUpdateByExampleStatementEnabled(false);
        config.setSelectByExampleQueryId("false");
        config.setSelectByPrimaryKeyStatementEnabled(true);
        config.setUpdateByPrimaryKeyStatementEnabled(true);
        config.setDeleteByPrimaryKeyStatementEnabled(true);
        if (StrUtil.isNotBlank(primaryKey)) {
            config.setGeneratedKey(new GeneratedKey(primaryKey, "JDBC", isIdentity, null));
        }
        if (properties != null) {
            for (GeneratorProperty property : properties) {
                config.addProperty(property.getName(), property.getValue());
            }
        }
        context.addTableConfiguration(config);
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
