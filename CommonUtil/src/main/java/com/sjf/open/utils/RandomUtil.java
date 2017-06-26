package com.sjf.open.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by xiaosi on 16-7-6.
 */
public class RandomUtil {

    private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numberChar = "0123456789";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomMixedStr(int length) {

        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }//for
        return sb.toString();

    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomLetterStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }//for
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯数字字符串
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static int getRandomNumStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }//for
        return Integer.parseInt(sb.toString());
    }

    public static Date getRandomDate(String beginDateStr, String endDateStr) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 构造开始日期
        Date beginDate = format.parse(beginDateStr);
        // 构造结束日期
        Date endDate = format.parse(endDateStr);
        if (beginDate.getTime() >= endDate.getTime()) {
            return null;
        }

        // 毫秒数
        long begin = beginDate.getTime();
        long end = endDate.getTime();
        long result = begin + (long) (Math.random() * (end - begin));
        return new Date(result);
    }

    public static String getRandomDateStr(String beginDateStr, String endDateStr){

        try {
            Date date = getRandomDate(beginDateStr, endDateStr);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void main(String[] args) throws ParseException {

        for(int i = 0;i < 10;i++){
            String date = getRandomDateStr("1990-01-01", "2017-02-08");
            System.out.println(date);
        }

    }
}
