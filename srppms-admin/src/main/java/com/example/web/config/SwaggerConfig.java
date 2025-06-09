package com.example.web.config;


import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements ApplicationListener<WebServerInitializedEvent> {
    private final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
    @Value("${srppms.name}")
    public String title;
    // @Value("${srppms.version}")
    //    public String version;
    @Value("${srppms.author}")
    public String author;
    @Value("${srppms.email}")
    public String email;
    @Autowired
    private RedisUtils redisUtils;

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfoAdmin() {
        return getApiInfoBuilder("srppms系统-主模块").build();
    }

    @Bean()
    public Docket docket1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("srppms-admin")
                .apiInfo(apiInfoAdmin())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.web.controller"))
                .build();
    }

    private ApiInfoBuilder getApiInfoBuilder(String description) {
        String version = redisUtils.get(RedisConstant.VERSION).toString();
        if (StringUtils.isEmpty(version)) {
            version = "1.0.0";
        }
        return new ApiInfoBuilder()
                //标题
                .title(title)
                //版本
                .version(version)
                //简介
                .description(description)
                //作者信息
                .contact(new Contact(author, "", email));
    }

    @Bean()
    public Docket docket2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("srppms-common")
                .apiInfo(apiInfoCommon())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.common.controller"))
                .build();
    }

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfoCommon() {
        return getApiInfoBuilder("srppms系统-通用模块").build();
    }

    @Bean()
    public Docket docket3() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("srppms-generator")
                .apiInfo(apiInfoGenerator())
                .select().apis(RequestHandlerSelectors.basePackage("com.example.generator.controller"))
                .build();
    }

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfoGenerator() {
        return getApiInfoBuilder("srppms系统-生成代码模块").build();
    }

    @Bean()
    public Docket docket4() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("srppms-mail")
                .apiInfo(apiInfoMail())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.mail.controller"))
                .build();
    }

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfoMail() {
        return getApiInfoBuilder("srppms系统-邮箱模块").build();
    }

    @Bean()
    public Docket docket5() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("srppms-system")
                .apiInfo(apiInfoSystem())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.system.controller"))
                .build();
    }

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfoSystem() {
        return getApiInfoBuilder("srppms系统-功能模块").build();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            //获取IP
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //获取端口号
            // int port = event.getWebServer().getPort();
            //获取应用名
            String applicationName = event.getApplicationContext().getApplicationName();
//            http://localhost:8888/doc.html
            String url = "http://" + hostAddress + ":" + event.getWebServer().getPort() + applicationName + "/doc.html";
            logger.info("Swagger启动成功！接口文档地址: {}", url);
            String key = RedisConstant.DICT + "swagger";
            redisUtils.set(key, url, -1);
        } catch (UnknownHostException e) {
//            e.printStackTrace();
            System.out.println("Exception:" + e);
        }
    }
}