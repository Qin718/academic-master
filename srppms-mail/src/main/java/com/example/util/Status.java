package com.example.util;

import com.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class Status {
    @Value("${srppms.author}")
    public String author;
    @Value("${srppms.name}")
    public String name;
    @Autowired
    private MailService mailService;

    /**
     * 启动
     */
    @PostConstruct
    public void firing() {
        String status = "启动";
        mailService.sendStatusMail(status, author, name);
    }

    /**
     * 关闭
     */
    @PreDestroy
    public void shutdown() {
        String status = "关闭";
        mailService.sendStatusMail(status, author, name);

    }


}
