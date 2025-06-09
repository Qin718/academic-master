package com.example.system.service;

import com.example.common.response.R;

import java.util.Map;

public interface CodeService {
    R<Map<String, Object>> getCode();

    R<String> getCodeMail(String qq);
}
