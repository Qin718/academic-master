package com.example.common.utils;


import javax.servlet.http.HttpServletRequest;

/**
 * 从http请求中获取操作系统
 */

public class OSUtils {
    public static String getOs(HttpServletRequest request) {
        return request.getHeader("OS");
    }

}

