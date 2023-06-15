package org.zsz.mybatiscodegen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.service.DbService;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author zsz
 * @version 1.0
 * 命令行入口
 */
// @Component
public class CommandEntry implements CommandLineRunner {

    @Autowired
    private DbService dbService;
    @Autowired
    private Environment environment;

    @Override
    @SuppressWarnings("all")
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========================================");
        String sqliteConnStr = environment.getProperty("spring.datasource.url");
        String reg = "^jdbc:sqlite:(\\w:/.+.db)$";
        if (ReUtil.isMatch(reg, sqliteConnStr)) {
            String dbFilePath = ReUtil.getGroup1(reg, sqliteConnStr);
            File dbFile = new File(dbFilePath);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
                System.out.println("创建完成");
            }
        }
        dbService.initUserDb();
        while (true) {
            runCmd(scanner);
        }
    }

    private void runCmd(Scanner scanner) {
        List<DbUsers> dbUsersList = dbService.selectList();
        System.out.println("已保存的数据库连接:");
        if (CollUtil.isNotEmpty(dbUsersList)) {
            for (DbUsers dbUsers : dbUsersList) {
                System.out.println(StrUtil.format("{}- Url: {}:{}, DB: {}, Driver: {}",
                        dbUsers.getUsersId(),
                        dbUsers.getUsersUrl(),
                        dbUsers.getUsersPort(),
                        dbUsers.getUsersDb(),
                        dbUsers.getUsersDriver()));
            }
        } else {
            System.out.println("-");
        }
        System.out.println("操作:");
        System.out.println("1 新增数据库连接");
        System.out.println("2 列出所有表");
        System.out.println("q 退出");
        String cmd = scanner.nextLine().trim();
        switch (cmd) {
            case "1":
                // 127.0.1.1 3306 root 123456 user_info org.mariadb.jdbc.Driver
                System.out.println("输入: url 端口 用户名 密码 数据库名称 驱动 数据库类型(1-ORACLE, 2-MYSQL)");
                String dbInfo = scanner.nextLine().trim();
                Pattern compile = Pattern.compile("^(.+?) (.+?) (.+?) (.+?) (.+?) (.+?) (.+?)$");
                List<String> groups = ReUtil.getAllGroups(compile, dbInfo, false);
                if (CollUtil.isEmpty(groups)) {
                    System.out.println("格式错误");
                    break;
                }
                dbService.insert(DbUsers.builder()
                        .usersUrl(groups.get(0))
                        .usersPort(groups.get(1))
                        .usersUid(groups.get(2))
                        .usersPwd(groups.get(3))
                        .usersDb(groups.get(4))
                        .usersDriver(groups.get(5))
                        .dbType(Integer.valueOf(groups.get(6)))
                        .build());
                break;
            case "q":
                System.exit(0);
                break;
            default:
                break;
        }
        clearConsole();
        System.out.println("========================================");
    }

    /**
     * 清屏
     */
    private void clearConsole() {
        // 将spring boot启动类的
        // SpringApplication.run(Xxx.class,args);
        // 改为：
        // SpringApplicationBuilder builder = new SpringApplicationBuilder(Xxx.class);
        // builder.headless(false).run(args);
        // Headless模式是系统的一种配置模式。在系统可能缺少显示设备、键盘或鼠标这些外设的情况下可以使用该模式。
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_CONTROL);             // 按下Ctrl键
            r.keyPress(KeyEvent.VK_R);                    // 按下R键
            r.keyRelease(KeyEvent.VK_R);                  // 释放R键
            r.keyRelease(KeyEvent.VK_CONTROL);            // 释放Ctrl键
            r.delay(100);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
