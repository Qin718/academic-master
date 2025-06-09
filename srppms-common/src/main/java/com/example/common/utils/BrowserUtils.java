package com.example.common.utils;


import javax.servlet.http.HttpServletRequest;

/**
 * 从http请求中获取浏览器信息
 */

public class BrowserUtils {
    public static String getBrowser(HttpServletRequest request) {
        return request.getHeader("Browser");
    }

}

