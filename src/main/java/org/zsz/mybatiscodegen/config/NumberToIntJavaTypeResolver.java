package org.zsz.mybatiscodegen.config;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * @author zsz
 * @version 1.0
 * @create 2023/4/25 15:54
 * @description
 */
public class NumberToIntJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    /**
     * INTEGER、SMALLINT和TINYINT都映射为Integer
     */
    public NumberToIntJavaTypeResolver() {
        super();
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT",
                new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                new FullyQualifiedJavaType(Integer.class.getName())));
    }
}
