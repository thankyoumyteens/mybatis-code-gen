package org.zsz.mybatiscodegen;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class MybatisCodeGenApplication {

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(Arrays.toString(args));
        ConfigurableApplicationContext context = SpringApplication.run(MybatisCodeGenApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        if (StrUtil.isBlank(port)) {
            port = "8080";
        }
        String path = "/main/index";
        log.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t" +
                "----------------------------------------------------------");
    }

}
