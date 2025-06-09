package com.example.system.others.utils;

import com.example.common.constant.SystemConstant;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Api(value = "通用检查方法", tags = "通用检查方法")
public class CheckUtils {

    /**
     * 检测是否包含数字
     */
    public static boolean checkContainDigit(String s) {
        char[] chPass = s.toCharArray();
        for (char pass : chPass) {
            if (Character.isDigit(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否包含特殊符号
     */
    public static boolean checkContainSpecialChar(String s) {
        char[] chPass = s.toCharArray();
        for (char pass : chPass) {
            if (SystemConstant.SPECIAL_CHAR.indexOf(pass) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否包含汉字
     */
    public static boolean checkContainChineseCharacters(String s) {
//        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Pattern p = Pattern.compile("[一-龥]");
        Matcher m = p.matcher(s);
        return m.find();
    }

    /**
     * 检测是否包含字母（不区分大小写）
     */
    public static boolean checkContainCase(String s) {
        return checkContainLowerCase(s) || checkContainUpperCase(s);
    }

    /**
     * 检测是否包含小写字母
     */
    public static boolean checkContainLowerCase(String s) {
        char[] chPass = s.toCharArray();
        for (char pass : chPass) {
            boolean isLowerCase = pass >= 'a' && pass <= 'z';
            if (isLowerCase) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否包含大写字母
     */
    public static boolean checkContainUpperCase(String s) {
        char[] chPass = s.toCharArray();
        for (char pass : chPass) {
            boolean isUpperCase = pass >= 'A' && pass <= 'Z';
            if (isUpperCase) {
                return true;
            }
        }
        return false;
    }
}
