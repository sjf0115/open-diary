package com.sjf.open.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Map;

/**
 * Created by xiaosi on 16-7-9.
 */
public class MapDemo {
    private static void testMultimap(){
        Multimap<String,Integer> multimap = ArrayListMultimap.create();
        Map<String,Integer> map = Maps.newHashMap();

        map.put("hotel",20);
        map.put("flight",10);

        multimap.put("hotel",10);
        multimap.put("train",23);

        for(String key : map.keySet()){
            multimap.put(key,map.get(key));
        }//for

        System.out.println(multimap.toString());
    }

    public static void main(String[] args) {
        testMultimap();
    }
}
