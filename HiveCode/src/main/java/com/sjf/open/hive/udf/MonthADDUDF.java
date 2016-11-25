package com.sjf.open.hive.udf;

import avro.shaded.com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by xiaosi on 16-11-11.
 */
public class MonthADDUDF extends UDF{

    // 支持默认两种格式
    private String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private String DEFAULT_SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    public Text evaluate(Text text, Integer num){

        if(Objects.equal(text, null)){
            return text;
        }

        Text result = evaluate(text, num, DEFAULT_DATE_FORMAT);
        if(Objects.equal(result, null)){
            result = evaluate(text, num, DEFAULT_SIMPLE_DATE_FORMAT);
        }
        return result;

    }

    public Text evaluate(Text text, Integer num, String format){

        if(Objects.equal(text, null)){
            return text;
        }

        if(StringUtils.isBlank(format)){
            return null;
        }

        String date = text.toString();
        DateTime dateTime = str2DateTime(date, format);
        if(Objects.equal(dateTime, null)){
            return null;
        }

        DateTime newDateTime;
        if(num < 0){
            newDateTime = dateTime.minusMonths(num * -1);
        }
        else{
            newDateTime = dateTime.plusMonths(num);
        }

        String newDate = newDateTime.toString(format);
        return new Text(newDate);
    }

    /**
     * 字符串时间转换为DateTime时间
     * @param dateStr 字符串时间
     * @param format 字符串时间格式
     * @return
     */
    public static DateTime str2DateTime(String dateStr, String format){

        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        try{
            DateTime dateTime = formatter.parseDateTime(dateStr);
            return dateTime;
        }
        catch (Exception e){
            return null;
        }

    }

}
