package com.sjf.open.demo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
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

    public static void main(String[] args) {
        reduceTest();
    }

}