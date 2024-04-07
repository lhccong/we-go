package com.cong.wego.utils;

/**
 * 常用实用程序
 *
 * @author cong
 * @date 2024/03/05
 */
public class CommonUtils {

    private CommonUtils() {

    }
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 正则表达式，表示字符串完全由数字组成
        String regex = "\\d+";
        return str.matches(regex);
    }

    public static boolean isNumericExceptLastS(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 正则表达式，表示字符串前面部分全是数字，最后一位是字母s
        String regex = "\\d+s$";
        return str.matches(regex);
    }
}
