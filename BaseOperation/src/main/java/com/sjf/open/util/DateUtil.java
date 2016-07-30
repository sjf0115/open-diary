package com.sjf.open.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 16-7-22.
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static String FULL_DATE_FOMAT = "yyyy/MM/dd HH:mm:ss";
    private static String SIMPLE_DATE_FOMAT = "yyyyMMdd";
    /**
     * 字符串时间转换为DateTime
     * @param dateStr
     * @return
     */
    public static DateTime str2DateTime(String dateStr, String format){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = null;
        try{
            dateTime = formatter.parseDateTime(dateStr);
        }
        catch (Exception e){
            logger.error("---------------- Invalid format {}",e);
        }
        return dateTime;
    }

    /**
     * 时间间隔 天
     * @param dateTimeOne
     * @param dateTimeTwo
     * @return
     */
    public static int daysBetween(DateTime dateTimeOne, DateTime dateTimeTwo){
        int offset = Days.daysBetween(dateTimeOne.toLocalDate(), dateTimeTwo.toLocalDate()).getDays();
        return offset;
    }

    /**
     * 计算指定日期距离今天的天数
     * @param dateTime
     * @return
     */
    public static int daysBetweenToday(DateTime dateTime){
        DateTime dateTimeToday = new DateTime();
        return daysBetween(dateTime, dateTimeToday);
    }

    public static int daysBetweenToday(String dateStr, String format){
        DateTime dateTimeToday = new DateTime();
        DateTime dateTimeTarget = str2DateTime(dateStr, format);
        return daysBetween(dateTimeTarget, dateTimeToday);
    }

    public static int daysBetweenToday(String dateStr){
        return daysBetweenToday(dateStr, SIMPLE_DATE_FOMAT);
    }

    /**
     * 检查yyyy-mm-dd hh:mm:ss形式的日期
     *
     * @param dateTimeStr
     * @return
     */
    private static boolean checkDateTime(String dateTimeStr) {
        Pattern pattern = Pattern.compile(
                "^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01]) (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})$");
        Matcher matcher = pattern.matcher(dateTimeStr);
        return matcher.matches();
    }

    public static void main(String[] args) {
//        System.out.println(checkDateTime("2016-07-12 12:34:32"));
        System.out.println(daysBetweenToday("20160709"));
    }
}
