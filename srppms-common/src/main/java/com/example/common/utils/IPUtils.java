package com.example.common.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从http请求中获取ip地址
 */

public class IPUtils {
    /**
     * 判断 公网 还是 内网
     */
    public static String innerIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return "无 IP";
        }

        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(ip);

        return match.find() ? "内网 IP" : "公网 IP" ;
    }

    /**
     * 获取IP地址
     */
    public static String getIp(HttpServletRequest request) {
        return request.getHeader("Ip");
    }
}

