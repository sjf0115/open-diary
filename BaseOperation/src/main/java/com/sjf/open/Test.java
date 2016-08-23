package com.sjf.open;

/**
 * Created by xiaosi on 16-8-16.
 */
public class Test {
    public static void main(String[] args) {
        String str = "玉树地区|玉树藏族自治州|玉树州|玉树县";
        String str2 = "aa|bb";
        String[] arr = str.split("\\|");
        for(String city : arr){
            System.out.println(city);
        }
    }
}
