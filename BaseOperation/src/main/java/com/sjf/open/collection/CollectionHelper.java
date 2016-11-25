package com.sjf.open.collection;

import avro.shaded.com.google.common.collect.Lists;
import avro.shaded.com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaosi on 16-11-11.
 */
public class CollectionHelper {
    public static void main(String[] args) {
        Map<String, Boolean> map = Maps.newHashMap();
        /*map.put("apple", false);
        map.put("banana", true);
        map.put("orange", true);*/
        Set<String> set = Collections.newSetFromMap(map);
        set.add("apple");
        set.add("apple");
        set.add("banana");
        System.out.println("map---" + map);
        System.out.println("set---" + set);

        Set<String> s = Sets.newHashSet();
        s.add("apple");
        s.add("apple");
        s.add("banana");
        System.out.println("s---"+s);

        Map<String, String> m = Maps.newHashMap();
        m.put("apple", "a");
        m.put("apple", "ap");
        m.put("banana", "b");
        System.out.println("m---"+m);

        ArrayList<String> list = Lists.newArrayList();
        list.add("apple");
        list.add("banana");
        list.add("orange");

        ArrayList<String> list2 = list;
        ArrayList<String> list3 = (ArrayList<String>) list.clone();

        //list.remove(1);
        remove(list);

        System.out.println("list----" + list);
        System.out.println("list2---" + list2);
        System.out.println("list3---" + list3);

    }

    public static void remove(ArrayList<String> arrayList){
        arrayList.remove(1);
    }
}
