package org.zsz.mybatiscodegen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsz
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DbUsers {

    /**
     * 主键
     */
    private Integer usersId;
    /**
     * 数据库url
     */
    private String usersUrl;
    /**
     * 数据库端口
     */
    private String usersPort;
    /**
     * 数据库用户名
     */
    private String usersUid;
    /**
     * 数据库密码
     */
    private String usersPwd;
    /**
     * 连接哪个数据库
     */
    private String usersDb;
    /**
     * jdbc驱动类
     */
    private String usersDriver;
    /**
     * 数据库类型
     */
    private Integer dbType;
}
