package com.sjf.open.stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by xiaosi on 17-3-1.
 */
public class SocketSparkStreaming implements Serializable {

    private static String hostName = "localhost";
    private static int port = 7777;

    public static void main(String[] args) {
        try {
            new SocketSparkStreaming().sparkStream(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sparkStream(int seconds) throws InterruptedException {

        SparkConf conf = new SparkConf().setAppName("socket-spark-stream").setMaster("local[2]");

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(seconds));
        // 以端口7777作为输入源创建DStream
        JavaReceiverInputDStream<String> lines = jsc.socketTextStream(hostName, port);
        // 从DStream中将每行文本切分为单词
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String x) {
                return Arrays.asList(x.split(" ")).iterator();
            }
        });

        // 在每个批次中计算单词的个数
        JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<>(s, 1);
            }
        });

        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        // 将此DStream中生成的每个RDD的前10个元素打印到控制台
        wordCounts.print();

        // 启动流计算环境StreamingContext并等待完成
        jsc.start();
        // 等待作业完成
        jsc.awaitTermination();
    }

}
