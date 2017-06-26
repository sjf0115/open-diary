package com.sjf.open.utils;

/**
 * Created by xiaosi on 16-12-23.
 */
public class NumUtil {

    /**
     * 10进制转换为16进制字符串
     * @param num
     * @return
     */
    public static String toHexString(int num){

        return Integer.toHexString(num);

    }

    /**
     *
     * @param str
     * @param radix
     * @return
     */
    public static int toDecimalNum(String str, int radix){

        int num = Integer.parseInt(str, radix);
        return num;
    }

}
