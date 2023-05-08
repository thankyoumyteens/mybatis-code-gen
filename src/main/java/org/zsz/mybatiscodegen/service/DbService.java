package org.zsz.mybatiscodegen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.entity.DbUsers;

import java.util.List;

/**
 * @author zsz
 * @version 1.0
 */
@Component
public class DbService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUserDb() {
        jdbcTemplate.update("DROP TABLE IF EXISTS \"db_users\";");
        String createDbUsers = "CREATE TABLE \"db_users\" (\n" +
                "\t\"users_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"users_url\" VARCHAR(256) NOT NULL,\n" +
                "\t\"users_port\" VARCHAR(256) NOT NULL,\n" +
                "\t\"users_uid\" VARCHAR(256) NOT NULL,\n" +
                "\t\"users_pwd\" VARCHAR(256) NOT NULL,\n" +
                "\t\"users_db\" VARCHAR(256) NOT NULL,\n" +
                "\t\"users_driver\" INTEGER NOT NULL,\n" +
                "\t\"db_type\" VARCHAR(256) NOT NULL\n" +
                ");";
        jdbcTemplate.update(createDbUsers);
    }

    public void insert(DbUsers users) {
        String insertSql = "INSERT INTO \"db_users\" (\"users_url\", \"users_port\", \"users_uid\", \"users_pwd\", \"users_db\", \"users_driver\", \"db_type\") VALUES\n" +
                "\t(?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(insertSql,
                users.getUsersUrl(),
                users.getUsersPort(),
                users.getUsersUid(),
                users.getUsersPwd(),
                users.getUsersDb(),
                users.getUsersDriver(),
                users.getDbType());
    }

    public List<DbUsers> selectList() {
        return jdbcTemplate.query("select * from db_users",
                BeanPropertyRowMapper.newInstance(DbUsers.class));
    }
}
