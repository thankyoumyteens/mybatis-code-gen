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
        jdbcTemplate.update("DROP TABLE IF EXISTS `db_users`;");
        String createDbUsers = "CREATE TABLE `db_users` (" +
                "`users_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`users_url` VARCHAR(256) NOT NULL," +
                "`users_port` VARCHAR(256) NOT NULL," +
                "`users_uid` VARCHAR(256) NOT NULL," +
                "`users_pwd` VARCHAR(256) NOT NULL," +
                "`users_db` VARCHAR(256) NOT NULL," +
                "`users_driver` INTEGER NOT NULL," +
                "`db_type` VARCHAR(256) NOT NULL" +
                ");";
        jdbcTemplate.update(createDbUsers);
    }

    public void insert(DbUsers users) {
        String insertSql = "insert into db_users(users_url,users_port,users_uid,users_pwd,users_db,users_driver,db_type) " +
                "values (?,?,?,?,?,?,?);";
        jdbcTemplate.update(insertSql,
                users.getUsersUrl(),
                users.getUsersPort(),
                users.getUsersUid(),
                users.getUsersPwd(),
                users.getUsersDb(),
                users.getUsersDriver(),
                users.getDbType());
    }

    public void del(int id) {
        String delSql = "delete from db_users where users_id = ?";
        jdbcTemplate.update(delSql, id);
    }

    public List<DbUsers> selectList() {
        return jdbcTemplate.query("select * from db_users",
                BeanPropertyRowMapper.newInstance(DbUsers.class));
    }
}
