package com.sjf.open.util;

/**
 * Created by xiaosi on 16-7-27.
 */
public class StringUtil {
    private static String weaterStr = "0029029070999991901010106004+64333+023450FM-12+000599999V0202701N015919999999N0000001N9-00781+99999102001ADDGF108991999999999999999999";
    private static void test(){
        System.out.println("Year:" + weaterStr.substring(15,19));
    }

    public static void main(String[] args) {
        test();
    }
}
