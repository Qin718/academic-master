package com.example.service;

import lombok.SneakyThrows;

public interface MailService {
    @SneakyThrows
    String sendCodeMail(String sendTo, String code);

    void sendStatusMail(String status, String author, String name);
}
