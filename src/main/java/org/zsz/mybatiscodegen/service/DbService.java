package org.zsz.mybatiscodegen.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.constant.DbType;
import org.zsz.mybatiscodegen.entity.DbUsers;

import java.io.File;
import java.util.List;

/**
 * @author zsz
 * @version 1.0
 */
@Component
public class DbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Environment environment;

    public void initUserDb() {
        String dbFileUrl = environment.getProperty("spring.datasource.url");
        System.out.println(dbFileUrl);
        String dbFileFolder = ReUtil.getGroup1("jdbc:sqlite:(.+)/.+\\.db", dbFileUrl);
        File file = new File(dbFileFolder);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("created db file:" + mkdirs);
        }
        String query = "SELECT * FROM sqlite_master WHERE type=\"table\" AND name = \"db_users\"";
        List<Object> dbList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Object.class));
        if (CollUtil.isEmpty(dbList)) {
            // jdbcTemplate.update("DROP TABLE IF EXISTS `db_users`;");
            String createDbUsers = "CREATE TABLE `db_users` (" +
                    "`users_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`users_url` VARCHAR(256) NOT NULL," +
                    "`users_port` VARCHAR(256) NOT NULL," +
                    "`users_uid` VARCHAR(256) NOT NULL," +
                    "`users_pwd` VARCHAR(256) NULL," +
                    "`users_db` VARCHAR(256) NOT NULL," +
                    "`users_driver` INTEGER NOT NULL," +
                    "`db_type` INTEGER NOT NULL" +
                    ");";
            jdbcTemplate.update(createDbUsers);
        }
    }

    public void insert(DbUsers users) {
        switch (users.getDbType()) {
            case DbType.ORACLE:
                users.setUsersDriver("oracle.jdbc.driver.OracleDriver");
                break;
            case DbType.MYSQL:
                users.setUsersDriver("com.mysql.jdbc.Driver");
                break;
            case DbType.MARIADB:
                users.setUsersDriver("org.mariadb.jdbc.Driver");
                break;
            default:
                throw new RuntimeException("数据库类型未知");
        }
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

    public <T> List<T> selectList(String sql, Class<T> mappedClass, Object... params) {
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(mappedClass), params);
    }

    public DbUsers selectById(Integer id) {
        return jdbcTemplate.queryForObject("select * from db_users where users_id=?",
                BeanPropertyRowMapper.newInstance(DbUsers.class), id);
    }
}
