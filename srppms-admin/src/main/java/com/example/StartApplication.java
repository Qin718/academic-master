package com.example;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.**", "com.example.generator.controller"})
@MapperScan(value = {"com.example.system.mapper", "com.example.generator.mapper"})
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
        System.out.println(
                "     (♥◠‿◠)ﾉﾞ         科研项目管理系统启动成功         ლ(´ڡ`ლ)ﾞ  \n" +
                        " ____  __._____.___.____  ______________      ___________________ \n" +
                        "|    |/ _|\\__  |   |\\   \\/  /\\__    ___/      \\_   ___ \\______   \\\n" +
                        "|      <   /   |   | \\     /   |    |  ______ /    \\  \\/|    |  _/\n" +
                        "|    |  \\  \\____   | /     \\   |    | /_____/ \\     \\___|    |   \\\n" +
                        "|____|__ \\ / ______|/___/\\  \\  |____|          \\______  /______  /\n" +
                        "        \\/ \\/             \\_/                         \\/       \\/ ");
    }
}
