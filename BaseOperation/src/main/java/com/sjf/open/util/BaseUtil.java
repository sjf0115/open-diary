package com.sjf.open.util;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by xiaosi on 16-10-12.
 */
public class BaseUtil {
    /**
     * 判断是否为null 否则转换为字符串
     * @param object
     * @return
     */
    public static String isBlank(Object object){

        if(Objects.equal(object, null)){
            return "";
        }
        return object.toString();

    }

    /**
     * 转换为String
     * @param object
     * @return
     */
    public static String toString(Object object){

        if(Objects.equal(object, null)){
            return "";
        }
        return object.toString();

    }

    /**
     * 转换为double
     * @param object
     * @return
     */
    public static double toDouble(Object object){

        if(Objects.equal(object, null)){
            return 0.0;
        }
        try{
            return Double.parseDouble(object.toString());
        }
        catch (Exception e){
            return 0.0;
        }

    }

    /**
     * 转换为int
     * @param object
     * @return
     */
    public static int toInt(Object object){

        if(Objects.equal(object, null)){
            return 0;
        }
        try{
            return Integer.parseInt(object.toString());
        }
        catch (Exception e){
            return 0;
        }

    }

    /**
     * 保留digit位小数
     * @param num
     * @param digit
     * @return
     */
    public static double formatDouble(double num, int digit) {

        if(digit < 0){
            return num;
        }

        // 如果不需要四舍五入 使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(num).setScale(digit, RoundingMode.HALF_UP);
        return bg.doubleValue();

    }

    /**
     * 保留digit位小数
     * @param num
     * @param digit
     * @return
     */
    public static String formatDouble2(double num, int digit) {

        if(digit <= 0){
            digit = 1;
        }

        String digitStr = Strings.repeat("0", digit);
        DecimalFormat   df = new DecimalFormat("#."+digitStr);
        return df.format(num);

    }

}
