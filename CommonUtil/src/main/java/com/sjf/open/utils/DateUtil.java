package com.sjf.open.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.google.common.base.Objects;

/**
 * DateTime具体类
 */
public class DateUtil {

    /**
     * 时间字符串转时间戳
     * 
     * @param date_str
     * @param format
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间 格式化 yyyy-MM-dd
     * 
     * @param dateStr 时间
     * @param format 源时间格式
     * @return
     */
    public static String dateFormat(String dateStr, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(dateStr);
            String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
            return dateFormat;
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 时间 格式化
     *
     * @param dateStr 时间
     * @param sourceFormat 源时间格式
     * @param targetFormat 目标时间格式
     * @return
     */
    public static String dateFormat(String dateStr, String sourceFormat, String targetFormat) {
        try {
            Date date = new SimpleDateFormat(sourceFormat).parse(dateStr);
            String dateFormat = new SimpleDateFormat(targetFormat).format(date);
            return dateFormat;
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 时间 格式化
     * 
     * @param dateSource
     * @param format 目标格式化
     * @return
     */
    public static String dateFormat(Date dateSource, String format) {
        if (Objects.equal(dateSource, null) || StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateFormat = sdf.format(dateSource);
        return dateFormat;
    }

    /**
     * 时间差
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysBetween(String startDate, String endDate) {
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            return -1;
        }
        // 时间差
        DateTime endDay = new DateTime(startDate);
        DateTime startDay = new DateTime(endDate);
        int days = Days.daysBetween(startDay, endDay).getDays();
        return days;
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static DateTime date2DateTime(String date) {

        String dateStr = DateUtil.dateFormat(date, "yyyy-MM-dd");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            DateTime formatFirstOrderDateTime = new DateTime(dateStr);
            return formatFirstOrderDateTime;
        } catch (Exception e) {
            return null;
        }

    }

    public static String timeStamp2Date(String timestampString, String formats){

        try{
            Long timestamp = Long.parseLong(timestampString)*1000;
            String date = new SimpleDateFormat(formats).format(new Date(timestamp));
            return date;
        }
        catch (Exception e){
            return null;
        }

    }


}
