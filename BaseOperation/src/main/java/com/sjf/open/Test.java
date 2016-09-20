package com.sjf.open;

/**
 * Created by xiaosi on 16-8-16.
 */
public class Test {
    public static void main(String[] args) {
        String str = "MU733|经停";
        String str2 = "aa|bb";
        String[] arr = str.split("\\|");
        System.out.println(arr[0]);
    }
}
