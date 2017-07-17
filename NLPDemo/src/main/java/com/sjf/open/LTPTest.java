package com.sjf.open;

import edu.hit.ir.ltp4j.Segmentor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaosi on 17-7-15.
 */
public class LTPTest {
    public static void main(String[] args) {

        int result = Segmentor.create("/home/xiaosi/code/ltp4j/ltp_data/cws.model");
        if(result < 0){
            System.err.println("load failed");
            return;
        }

        String sent = "我是中国人";
        List<String> words = new ArrayList<String>();
        int size = Segmentor.segment(sent,words);

        for(int i = 0; i<size; i++) {
            System.out.print(words.get(i));
            if(i==size-1) {
                System.out.println();
            } else{
                System.out.print("\t");
            }
        }
        Segmentor.release();
    }
}
