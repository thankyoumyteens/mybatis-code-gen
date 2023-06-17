package org.zsz.mybatiscodegen.plugin;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zsz
 * @version 1.0
 * 如果列是字符串类型，则把if标签中的 test"col != null" 替换成 test="col != null and col != ''"
 */
public class IfWithStringNotEmptyTestPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * mapper文件生成之后修改mapper文件的内容
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        // 表中的所有列
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        Map<String, IntrospectedColumn> columnMap = allColumns.stream().collect(Collectors.toMap(IntrospectedColumn::getJavaProperty, v -> v));
        // 修改生成的 Mapper XML 文件
        List<VisitableElement> elements = document.getRootElement().getElements();
        for (VisitableElement element : elements) {
            if (element instanceof XmlElement) {
                processXmlElement((XmlElement) element, columnMap);
            }
        }
        return true;
    }

    private void processXmlElement(XmlElement element, Map<String, IntrospectedColumn> columnMap) {
        if (element.getName().equalsIgnoreCase("if")) {
            // 改变 if 标签的 test 属性
            List<Attribute> attributes = element.getAttributes();
            Optional<Attribute> testAttr = attributes.stream().filter(v -> "test".equalsIgnoreCase(v.getName())).findAny();
            if (testAttr.isPresent()) {
                // 从字符串 col != null 中取出列名col
                String regex = "(.+?)\\s?!=\\s?null";
                String test = testAttr.get().getValue();
                if (ReUtil.isMatch(regex, test)) {
                    String columnName = ReUtil.getGroup1(regex, test);
                    if (columnMap.containsKey(columnName)) {
                        IntrospectedColumn introspectedColumn = columnMap.get(columnName);
                        FullyQualifiedJavaType javaType = introspectedColumn.getFullyQualifiedJavaType();
                        if ("java.lang.String".equalsIgnoreCase(javaType.toString())) {
                            // test"col != null" 替换成 test="col != null and col != ''"
                            element.getAttributes().clear();
                            element.addAttribute(new Attribute("test", StrUtil.format("{} != null and {} != ''", columnName, columnName)));
                        }
                    }
                }
            }
        }
        // 递归处理子元素
        List<VisitableElement> elements = element.getElements();
        for (VisitableElement e : elements) {
            if (e instanceof XmlElement) {
                processXmlElement((XmlElement) e, columnMap);
            }
        }
    }
}
