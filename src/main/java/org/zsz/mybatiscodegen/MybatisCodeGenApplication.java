package org.zsz.mybatiscodegen;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MybatisCodeGenApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MybatisCodeGenApplication.class);
        builder.headless(false).run(args);
    }

}
