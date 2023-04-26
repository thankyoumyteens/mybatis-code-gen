package org.zsz.mybatiscodegen.config;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author zsz
 * @version 1.0
 * @create 2023/4/26 17:27
 * @description
 */
public class CustomCommentGenerator implements CommentGenerator {
    private final Properties properties;

    public CustomCommentGenerator() {
        properties = new Properties();
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // 获取自定义的 properties
        this.properties.putAll(properties);
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remarks);
            field.addJavaDocLine(" */");
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String author = properties.getProperty("author");
        String dateFormat = properties.getProperty("dateFormat");
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        topLevelClass.addJavaDocLine("/**");
        // 获取表注释
        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            topLevelClass.addJavaDocLine(" * " + remarks);
            topLevelClass.addJavaDocLine(" *");
        }
        topLevelClass.addJavaDocLine(" * @author: " + author);
        topLevelClass.addJavaDocLine(" * " + dateFormatter.format(new Date()));
        topLevelClass.addJavaDocLine(" */");
    }
}