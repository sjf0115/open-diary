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
 * Spark 转换操作 demo
 *
 */

public final class TransformationsRDDAPI {

    private static final Pattern SPACE = Pattern.compile(" ");

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

    public static void pairPrintIte(JavaPairRDD<String, Iterable<Integer>> rdd) {
        rdd.foreach(new VoidFunction<Tuple2<String, Iterable<Integer>>>() {
            @Override
            public void call(Tuple2<String, Iterable<Integer>> tuple) throws Exception {
                String key = tuple._1();
                Iterable<Integer> valueIte = tuple._2();
                Iterator<Integer> ite = valueIte.iterator();
                String value = "";
                while (ite.hasNext()) {
                    value += ite.next() + " ";
                } // while
                System.out.println(key + " --- " + value);
            }
        });
    }

    /**
     * 并行集合
     */
    public static void parallelCollections() {

        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        JavaRDD<Integer> rdd = sc.parallelize(list);
        Integer sum = rdd.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        System.out.println(sum);

    }

    /**
     * 外部数据集
     */
    public static void externalDataSets() {

        // 匿名内部类
        JavaRDD<String> lines2 = sc.textFile("/home/xiaosi/a.txt");
        JavaRDD<Integer> lineLengths2 = lines2.map(new Function<String, Integer>() {
            public Integer call(String s) {
                return s.length();
            }
        });
        int totalLength2 = lineLengths2.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        });
        System.out.println(totalLength2);

        // 命名内部类
        class GetLength implements Function<String, Integer> {
            public Integer call(String s) {
                return s.length();
            }
        }
        class Sum implements Function2<Integer, Integer, Integer> {
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        }

        JavaRDD<String> lines3 = sc.textFile("/home/xiaosi/a.txt");
        JavaRDD<Integer> lineLengths3 = lines3.map(new GetLength());
        int totalLength3 = lineLengths3.reduce(new Sum());
        System.out.println(totalLength3);
    }

    // wholeText
    public static void wholeText() {

        JavaPairRDD<String, String> rdd = sc.wholeTextFiles("/home/xiaosi/wholeText");
        List<Tuple2<String, String>> list = rdd.collect();
        for (Tuple2<?, ?> tuple : list) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

    }

    // hadoopRDD
    public static void hadoopRDDTest() {

        JobConf conf = new JobConf();
        JavaPairRDD<Text, Text> rdd = sc.hadoopRDD(conf, FileInputFormat.class, Text.class, Text.class);
        List<Tuple2<Text, Text>> list = rdd.collect();
        for (Tuple2<?, ?> tuple : list) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

    }

    // 映射
    public static void mapTest() {
        List<String> aList = Lists.newArrayList("a", "B", "c", "b");
        JavaRDD<String> rdd = sc.parallelize(aList);
        // 小写转大写
        JavaRDD<String> upperLinesRDD = rdd.map(new Function<String, String>() {
            @Override
            public String call(String str) throws Exception {
                if (StringUtils.isBlank(str)) {
                    return str;
                }
                return str.toUpperCase();
            }
        });
        // A B C B
        // 输出
        simplePrint(upperLinesRDD);
    }

    // 过滤
    public static void filterTest() {
        List<String> aList = Lists.newArrayList("a", "B", "c", "b");
        JavaRDD<String> rdd = sc.parallelize(aList);
        JavaRDD<String> filterRDD = rdd.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String str) throws Exception {
                return !str.startsWith("a");
            }
        });
        // 输出
        simplePrint(filterRDD);
    }

    // 一行转多行
    public static void flatMapTest() {
        List<String> list = Lists.newArrayList("a 1", "B 2");
        JavaRDD<String> rdd = sc.parallelize(list);
        // 一行转多行 以空格分割
        JavaRDD<String> resultRDD = rdd.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                if (StringUtils.isBlank(s)) {
                    return null;
                }
                String[] array = s.split(" ");
                return Arrays.asList(array).iterator();
            }
        });
        // 输出
        simplePrint(resultRDD);
    }

    // 去重
    public static void distinctTest() {
        List<String> aList = Lists.newArrayList("1", "3", "2", "3");
        JavaRDD<String> aRDD = sc.parallelize(aList);
        // 去重
        JavaRDD<String> rdd = aRDD.distinct();
        // 输出
        simplePrint(rdd);
    }

    // 并集
    public static void unionTest() {
        List<String> aList = Lists.newArrayList("1", "2", "3");
        List<String> bList = Lists.newArrayList("3", "4", "5");
        JavaRDD<String> aRDD = sc.parallelize(aList);
        JavaRDD<String> bRDD = sc.parallelize(bList);
        // 并集
        JavaRDD<String> rdd = aRDD.union(bRDD);
        // 输出
        simplePrint(rdd);
    }

    // 交集
    public static void intersectionTest() {
        List<String> aList = Lists.newArrayList("1", "2", "3");
        List<String> bList = Lists.newArrayList("3", "4", "5");
        JavaRDD<String> aRDD = sc.parallelize(aList);
        JavaRDD<String> bRDD = sc.parallelize(bList);
        // 交集
        JavaRDD<String> rdd = aRDD.intersection(bRDD);
        // 输出
        simplePrint(rdd);
    }

    // 差集
    public static void subtractTest() {
        List<String> aList = Lists.newArrayList("1", "2", "3");
        List<String> bList = Lists.newArrayList("3", "4", "5");
        JavaRDD<String> aRDD = sc.parallelize(aList);
        JavaRDD<String> bRDD = sc.parallelize(bList);
        // 交集
        JavaRDD<String> rdd = aRDD.subtract(bRDD); // 1 2
        // 输出
        simplePrint(rdd);
    }

    // 分组
    public static void groupByKeyTest() {
        Tuple2<String, Integer> t1 = new Tuple2<String, Integer>("Banana", 10);
        Tuple2<String, Integer> t2 = new Tuple2<String, Integer>("Pear", 5);
        Tuple2<String, Integer> t3 = new Tuple2<String, Integer>("Banana", 9);
        Tuple2<String, Integer> t4 = new Tuple2<String, Integer>("Apple", 4);
        List<Tuple2<String, Integer>> list = Lists.newArrayList();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);
        JavaPairRDD<String, Integer> rdd = sc.parallelizePairs(list);
        // 分组
        JavaPairRDD<String, Iterable<Integer>> groupRDD = rdd.groupByKey();
        // 输出
        pairPrintIte(groupRDD);
        // Apple --- 4
        // Pear --- 5
        // Banana --- 10 9
    }

    // 分组计算
    public static void reduceByKeyTest() {
        Tuple2<String, Integer> t1 = new Tuple2<String, Integer>("Banana", 10);
        Tuple2<String, Integer> t2 = new Tuple2<String, Integer>("Pear", 5);
        Tuple2<String, Integer> t3 = new Tuple2<String, Integer>("Banana", 9);
        Tuple2<String, Integer> t4 = new Tuple2<String, Integer>("Apple", 4);
        List<Tuple2<String, Integer>> list = Lists.newArrayList();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);
        JavaPairRDD<String, Integer> rdd = sc.parallelizePairs(list);
        // 分组计算
        JavaPairRDD<String, Integer> reduceRDD = rdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        // 输出
        pairPrint(reduceRDD);
        // Apple --- 4
        // Pear --- 5
        // Banana --- 19
    }

    // 分组聚合
    public static void aggregateByKeyTest() {
        Tuple2<String, Integer> t1 = new Tuple2<String, Integer>("Banana", 10);
        Tuple2<String, Integer> t2 = new Tuple2<String, Integer>("Pear", 5);
        Tuple2<String, Integer> t3 = new Tuple2<String, Integer>("Banana", 9);
        Tuple2<String, Integer> t4 = new Tuple2<String, Integer>("Apple", 4);
        List<Tuple2<String, Integer>> list = Lists.newArrayList();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);
        JavaPairRDD<String, Integer> rdd = sc.parallelizePairs(list);
        rdd.aggregateByKey(3, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return null;
            }
        }, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return null;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        // parallelCollections();
        // externalDataSets();
        // wholeText();
        // pairTest();
//        mapTest();
//         filterTest();
         flatMapTest();
        // distinctTest();
        // unionTest();
        // intersectionTest();
        // subtractTest();
        // groupByKeyTest();
        // reduceByKeyTest();
    }
}