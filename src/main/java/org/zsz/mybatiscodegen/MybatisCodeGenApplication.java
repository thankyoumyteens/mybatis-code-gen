package org.zsz.mybatiscodegen;

import cn.hutool.core.util.StrUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
public class MybatisCodeGenApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        applicationContext = new SpringApplicationBuilder(MybatisCodeGenApplication.class)
                // 使用javaFX时不启动web服务器
                .web(WebApplicationType.NONE)
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainFxml = getClass().getResource("/mainWindow.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(mainFxml);
        // 将Spring的应用程序上下文与FXML控制器关联起来，以便能够在控制器中使用Spring的依赖注入
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        // 加载FXML文件，并获取根节点
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX 应用");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(Arrays.toString(args));
        if (args.length != 0 && "fx".equals(args[0])) {
            // 运行javaFX
            launch(args);
        } else {
            // 运行spring boot
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

}
