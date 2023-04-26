package org.zsz.mybatiscodegen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsz
 * @version 1.0
 * @create 2023/4/26 15:20
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DbUsers {

    private Integer usersId;
    private String usersUrl;
    private String port;
    private String usersUid;
    private String usersPwd;
    private String usersDb;
    private String usersDriver;
    private Integer dbType;
}
