package com.example.service.impl;

import com.example.service.MailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    public String username;
    @Value("${spring.mail.password}")
    public String password;
    @Value("${spring.mail.host}")
    public String host;
    @Value("${spring.mail.default-encoding}")
    public String encoding;
    @Value("${spring.mail.open}")
    public Boolean open;
    @Value("${spring.html.code}")
    public String codeHtml;
    @Value("${spring.html.status}")
    public String statusHtml;
    @Autowired
    ResourceLoader resourceLoader;

    /**
     * 发送验证码邮件
     *
     * @param sendTo 发送给谁
     * @param code   验证码
     */
    @Override
    @SneakyThrows
    public String sendCodeMail(String sendTo, String code) {
        if (!open) {
            return "发送失败，邮箱功能未开启";
        }
        //1.创建参数配置，用于连接邮件服务器的参数配置
        Properties prop = getProperties();
        //2.根据配置创建会话
        Session session = Session.getInstance(prop);
        //读取验证码邮件模板
        Resource resource = resourceLoader.getResource("classpath:" + codeHtml);
        File file = resource.getFile();
        FileReader reader = new FileReader(file);
        //这里必须定义为整形，java规定io 里面的read()这个方法的返回值是整形的
        int i;
        //读取一个字符
        i = reader.read();
        StringBuilder content = new StringBuilder();
        while (i != (-1)) {
            //ASCII码是从0开始的数字，只有什么都没有才会返回-1
            content.append((char) i);
            //继续读取一个字符
            i = reader.read();
        }
        content = new StringBuilder(content.toString().replace("code", code));
        //标题
        String title = "验证码";
        //3.创建一封邮件
        MimeMessage message = createMimeMessage(title, content.toString(), session, username, sendTo);
        //4.根据Session获取邮件
        Transport transport = session.getTransport();
        //5.使用邮箱账号密码连接邮件服务器
        transport.connect(username, password);
        //6.发送邮件，发到所有的收件地址
        transport.sendMessage(message, message.getAllRecipients());
        //7.关闭连接
        transport.close();
        return "发送成功";
    }

    /**
     * 发送项目状态邮件
     *
     * @param status 状态
     * @param author 项目作者
     * @param name 项目名称
     */
    @SneakyThrows
    @Override
    public void sendStatusMail(String status, String author, String name) {
        if (!open) {
            return;
        }
        //1.创建参数配置，用于连接邮件服务器的参数配置
        Properties prop = getProperties();
        //2.根据配置创建会话
        Session session = Session.getInstance(prop);
        //读取验证码邮件模板
        Resource resource = resourceLoader.getResource("classpath:" + statusHtml);
        File file = resource.getFile();
        FileReader reader = new FileReader(file);
        //这里必须定义为整形，java规定io 里面的read()这个方法的返回值是整形的
        int i;
        //读取一个字符
        i = reader.read();
        StringBuilder content = new StringBuilder();
        //ASCII码是从0开始的数字，只有什么都没有才会返回-1
        while (i != (-1)) {
            content.append((char) i);
            //继续读取一个字符
            i = reader.read();
        }
        String time = Calendar.getInstance().getTime().toString();
        content = new StringBuilder(content.toString().replace("time", time).replace("status", status).replace("author", author).replace("name", name));
        //标题
        String title = "项目已" + status;
        //3.创建一封邮件
        MimeMessage message = createMimeMessage(title, content.toString(), session, username, username);
        //4.根据Session获取邮件
        Transport transport = session.getTransport();
        //5.使用邮箱账号密码连接邮件服务器
        transport.connect(username, password);
        //6.发送邮件，发到所有的收件地址
        transport.sendMessage(message, message.getAllRecipients());
        //7.关闭连接
        transport.close();
    }

    private Properties getProperties() {
        final String smtpPort = "465";
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.port", smtpPort);
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);
        return prop;
    }

    /**
     * 创建邮件
     *
     * @param title    标题
     * @param content  内容
     * @param session  会话
     * @param sendMail 发送人
     * @param sendTo   要发送给谁
     */

    public MimeMessage createMimeMessage(String title, String content, Session session, String sendMail, String sendTo) throws Exception {
        //1.创建一封邮件
        MimeMessage message = new MimeMessage(session);
        //2.from:发件人
        message.setFrom(new InternetAddress(sendMail, "发件人", encoding));
        //3.to:收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendTo, "收件人", encoding));
        //4.邮件主题
        message.setSubject(title, encoding);
        //5.邮件内容
        message.setContent(content, "text/html;charset=" + encoding);
        //6.设置发件时间
        message.setSentDate(new Date());
        //7.保存设置
        message.saveChanges();
        return message;
    }


}
