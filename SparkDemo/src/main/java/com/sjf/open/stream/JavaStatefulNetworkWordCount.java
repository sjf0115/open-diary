package com.sjf.open.stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 17-6-26.
 */
public class JavaStatefulNetworkWordCount {

    private static final Pattern SPACE = Pattern.compile(" ");

    private static String hostName = "localhost";
    private static int port = 7777;

    public static void main(String[] args) {
        try {
            sparkStream(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sparkStream(int seconds) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("socket-spark-stream").setMaster("local[2]");

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(seconds));

        // 初始RDD
        List<Tuple2<String, Integer>> tuples =
                Arrays.asList(new Tuple2<>("hello", 1), new Tuple2<>("world", 1));
        JavaPairRDD<String, Integer> initialRDD = jsc.sparkContext().parallelizePairs(tuples);

        // 以端口7777作为输入源创建DStream
        JavaReceiverInputDStream<String> lines = jsc.socketTextStream(hostName, port, StorageLevel.MEMORY_AND_DISK_SER_2());

        // 以空格进行分割成单词列表
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String x) {
                return Arrays.asList(SPACE.split(x)).iterator();
            }
        });

        // 单词映射成键值对(word, 1)
        JavaPairDStream<String, Integer> wordsDStream = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        // 定义状态更新函数
        Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>> updateStateFunction = new Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>>() {

            @Override
            public Tuple2<String, Integer> call(String word, Optional<Integer> one, State<Integer> state)
                    throws Exception {
                int sum = one.orElse(0) + (state.exists() ? state.get() : 0);
                Tuple2<String, Integer> output = new Tuple2<>(word, sum);
                state.update(sum);
                return output;
            }
        };

        // 更新状态
        JavaMapWithStateDStream<String, Integer, Integer, Tuple2<String, Integer>> stateDstream =
                wordsDStream.mapWithState(StateSpec.function(updateStateFunction).initialState(initialRDD));


        stateDstream.print();
        jsc.start();
        jsc.awaitTermination();
    }

}
