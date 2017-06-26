package com.sjf.open.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiaosi on 17-2-17.
 */
public class RegexUtil {

    /**
     * 是否匹配正则表达式
     * @param param
     * @param pattern
     * @return
     */
    public static boolean isFind(String param, Pattern pattern) {

        if(StringUtils.isBlank(param)){
            return false;
        }

        try {
            Matcher matcher = pattern.matcher(param);
            return matcher.find();
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 是否匹配正则表达式
     * @param param
     * @param reg
     * @return
     */
    public static boolean isFind(String param, String reg) {

        if(StringUtils.isBlank(param) || StringUtils.isBlank(reg)){
            return false;
        }

        Pattern pattern = Pattern.compile(reg);
        boolean result = isFind(param, pattern);
        return result;

    }

    public static String getGroupValue(String param, String reg, int group) {
        try {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(param);
            if (matcher.find()) {
                return matcher.group(group);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static String getGroupValue(String param, Pattern pattern, int group)  {
        try {
            Matcher matcher = pattern.matcher(param);
            if (matcher.find()) {
                return matcher.group(group);
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public static void main(String[] args) {
        String reg = "{\"__rT\":\"OperationResult\"*";
        String line = "";
    }
}

