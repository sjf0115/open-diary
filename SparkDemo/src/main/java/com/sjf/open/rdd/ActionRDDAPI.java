package com.sjf.open.rdd;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

import com.google.common.collect.Lists;

/**
 * Created by xiaosi on 17-2-13.
 *
 * Spark 动作操作 demo
 *
 */

public final class ActionRDDAPI {

    private static String appName = "JavaWordCountDemo";
    private static String master = "local";

    // 初始化Spark
    private static SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
    private static JavaSparkContext sc = new JavaSparkContext(conf);

    // 简单输出
    public static void simplePrint(JavaRDD<String> rdd) {
        rdd.foreach(new VoidFunction<String>() {
            @Override
            public void call(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    // 键值对输出
    public static void pairPrintStr(JavaPairRDD<String, String> rdd) {
        rdd.foreach(new VoidFunction<Tuple2<String, String>>() {
            @Override
            public void call(Tuple2<String, String> tuple2) throws Exception {
                System.out.println(tuple2._1() + " --- " + tuple2._2());
            }
        });
    }

    public static void pairPrint(JavaPairRDD<String, Integer> rdd) {
        rdd.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple2) throws Exception {
                System.out.println(tuple2._1() + " --- " + tuple2._2());
            }
        });
    }

    // reduce
    public static void reduceTest() {
        List<String> aList = Lists.newArrayList("aa", "bb", "cc", "dd");
        JavaRDD<String> rdd = sc.parallelize(aList);
        String result = rdd.reduce(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v1 + "#" + v2;
            }
        });
        System.out.println(result); // aa#bb#cc#dd
    }

    // collect
    public static void collectTest(){
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        JavaRDD<Integer> rdd = sc.parallelize(list);
        List<Integer> collect = rdd.collect();
        System.out.println(collect); // [1, 2, 3, 4, 5]
    }

    // take
    public static void takeTest(){
        List<String> list = Lists.newArrayList("aa", "bb", "cc", "dd");
        JavaRDD<String> rdd = sc.parallelize(list);
        List<String> collect = rdd.take(3);
        System.out.println(collect); // [aa, bb, cc]
    }


    public static void main(String[] args) {
//        reduceTest();
//        collectTest();
//        collectTest();

        List<Integer> list = Arrays.asList(1,2,3,4,5);
        JavaRDD<Integer> rdd = sc.parallelize(list);
        Integer resultRDD = rdd.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        System.out.println(resultRDD);
    }

}