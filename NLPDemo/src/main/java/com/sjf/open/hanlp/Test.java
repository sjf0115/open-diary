package com.sjf.open.hanlp;

import com.hankcs.hanlp.HanLP;

/**
 * Created by xiaosi on 17-7-15.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(HanLP.parseDependency("衣服面料非常好,做工精细"));
    }
}
