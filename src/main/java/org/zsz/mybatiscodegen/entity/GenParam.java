package org.zsz.mybatiscodegen.entity;

import lombok.Data;

@Data
public class GenParam {

    /**
     * 数据库连接信息表的主键
     */
    private Integer id;
    /**
     * 表明
     */
    private String tableName;
    /**
     * 使用Lombok
     */
    private Boolean useLombok;
    /**
     * 生成注释
     */
    private Boolean useComment;
}
