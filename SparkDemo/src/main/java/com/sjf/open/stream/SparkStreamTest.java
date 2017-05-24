package com.sjf.open.stream;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-2-28.
 */
public class SparkStreamTest implements Serializable{

    /*public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("SparkStream: <zookeeperIP> <groupID> <topic1,topic2,...> <numThreads> <seconds>");
            System.exit(1);
        }
        Map<String, Integer> topicsMap = new HashMap<String, Integer>();
        String[] topic = args[2].split(",");
        for (String t : topic) {
            topicsMap.put(t, 1);
        }
        String zookeeperIP = args[0];
        String groupID = args[1];
        int task = Integer.parseInt(args[3]);
        int seconds = Integer.parseInt(args[4]);

        new SparkStreamTest().startStream(topicsMap, zookeeperIP, groupID, task, seconds);
    }

    public void startStream(Map<String, Integer> topicsMap, String zookeeperIP, String groupID, int task, int seconds){

        SparkConf conf = new SparkConf().setAppName("spark-stream-test").setMaster("local[2]");
        conf.set("spark.streaming.blockInterval", "50ms");

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, Durations.seconds(seconds));

        Map<String, String> kafkaParams = Maps.newHashMap();
        kafkaParams.put("zookeeper.connect", zookeeperIP);
        kafkaParams.put("group.id", groupID);
        kafkaParams.put("zookeeper.connection.timeout.ms", "10000");

        JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils.createStream(streamingContext, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topicsMap, StorageLevels.MEMORY_AND_DISK);

        List<JavaPairDStream<String, String>> javaPairReceiverInputDStreamList = new ArrayList<JavaPairDStream<String, String>>();
        for (int i = 0; i < task - 1; i++) {
            JavaPairReceiverInputDStream<String, String> streamTask = KafkaUtils.createStream(streamingContext, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topicsMap, StorageLevels.MEMORY_AND_DISK);
            javaPairReceiverInputDStreamList.add(streamTask);
        }
        JavaPairDStream dStream = streamingContext.union(kafkaStream, javaPairReceiverInputDStreamList);

        new SparkStreamTest().receiveKafkaData(dStream);

        streamingContext.start();
        while (true) {
            try {
                Thread.sleep(10000);
                System.out.print("sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void receiveKafkaData(JavaPairDStream pairDStream) {

        JavaDStream data = pairDStream.map((Function<Tuple2<String, String>, String>) this::handleMessage)
                .filter((Function<String, Boolean>) StringUtils::isNotBlank);

        data.foreachRDD((VoidFunction2<JavaRDD<String>, Time>) (v1, v2) -> {

            if (v1 == null || v1.partitions().isEmpty()) {
                System.out.println("-------------");
                return;
            }
            v1.foreachPartition((VoidFunction<Iterator<String>>) stringIterator -> {
                while (stringIterator.hasNext()) {
                    String line = stringIterator.next();
                    analysisLog(line);
                }
            });
        });



    }

    private String handleMessage(Tuple2<String, String> message) {
        System.out.println("------------------");
        return "handleMessge --- " + message._2();
    }

    private static String analysisLog(String line) {
        String result = "analysisLog --- " + line;
        System.out.println(result);
        return result;
    }*/

}
