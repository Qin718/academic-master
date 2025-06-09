package com.example.generator.service.impl;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.example.common.response.R;
import com.example.generator.domain.Gen;
import com.example.generator.mapper.GenMapper;
import com.example.generator.service.GenService;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GenServiceImpl implements GenService {
    @Value("${spring.datasource.database}")
    private String database;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${srppms.author}")
    private String author;
    @Autowired
    private GenMapper genMapper;

    @Override
    public R<PageVo> getList(PageBo pageBo) {
        List<Gen> list = genMapper.getColumnDetailMapVo(database);
        return R.ok(new PageVo(pageBo, list));
    }

    @Override
    public R<String> create(String table) {
        String property = System.getProperty("user.dir")+"/srppms-system/src/main/java";
        FastAutoGenerator
                .create(url, username, password)
                .globalConfig(builder -> {
                    builder
                            .dateType(DateType.ONLY_DATE)//日期格式
                            .author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()// 不打开资源管理器
                            .outputDir(property); // 指定输出目录
                })
                .packageConfig(builder -> {
                    Map<OutputFile, String> pathInfo = new HashMap<>();
                    pathInfo.put(OutputFile.controller,
                            System.getProperty("user.dir") + "/srppms-admin/src/main/java/com/example/web/controller");
                    pathInfo.put(OutputFile.xml,
                            System.getProperty("user.dir") + "/srppms-system/src/main/resources/mapper");

                    String packageParent = "com.example";
                    String packageWeb = "web.";
                    String packageController = packageWeb + "controller";
                    String packageSystem = "system.";
                    String packageMapper = packageSystem + "mapper";
                    String packageDomain = packageSystem + "domain";
                    String packageEntity = packageDomain + ".entity";
                    String packageService = packageSystem + "service";
                    String packageServiceImpl = packageSystem + "service.impl";

                    builder
                            .parent(packageParent) // 设置父包名
                            .controller(packageController)
                            .service(packageService)
                            .serviceImpl(packageServiceImpl)
                            .mapper(packageMapper)
                            .entity(packageEntity)
                            .pathInfo(pathInfo);
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(table) // 设置需要生成的表名
                            .addTablePrefix("sys_") // 设置过滤表前缀

                            // Controller 策略配置
                            .controllerBuilder()
                            .enableRestStyle() // 开启@RestController
                            .formatFileName("%sController") // Controller 文件名称

                            // Service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service 文件名称
                            .formatServiceImplFileName("%sServiceImpl") // ServiceImpl 文件名称

                            // Mapper 策略配置
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启@Mapper
                            .enableBaseColumnList() // 启用 columnList (通用查询结果列)
                            .enableBaseResultMap() // 启动resultMap
                            .formatMapperFileName("%sMapper") // Mapper 文件名称
                            .formatXmlFileName("%sMapper"); // Xml 文件名称
                })
                .injectionConfig(consumer -> {
                    ArrayList<CustomFile> list = new ArrayList<>();
                    list.add(new CustomFile.Builder().fileName("Bo.java").templatePath("template/Bo.java.vm").packageName("system/domain/bo").build());
                    list.add(new CustomFile.Builder().fileName("Vo.java").templatePath("template/Vo.java.vm").packageName("system/domain/vo").build());
                    consumer.customFile(list);
                })
                .templateConfig(builder -> builder
                        .xml("/template/mapper.xml.vm")
                        .controller("/template/controller.java")// 设置生成controller的模板
                        .service("/template/service.java")// 设置生成service的模板
                        .serviceImpl("/template/serviceImpl.java")// 设置生成serviceImpl的模板
                        .mapper("/template/mapper.java") // 设置生成mapper的模板
                        .entity("/template/entity.java")// 设置生成entity的模板
                        .build())
                .execute();


        return R.ok("生成代码成功");
    }
}
