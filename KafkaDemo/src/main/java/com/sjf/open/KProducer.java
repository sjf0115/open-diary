package com.sjf.open;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by xiaosi on 16-8-2.
 */
public class KProducer{

    private static Logger logger = LoggerFactory.getLogger(KProducer.class);

    private Producer<String, String> producer;
    private String topic;

    public KProducer(String topic) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);

        this.topic = topic;
    }

    public void run() {

        for(int i = 0;i < 100;i++){
            String message = "message->" + i;
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
            try{
                producer.send(producerRecord);
            }
            catch (Exception e){
                logger.error("------------------ KProducer --- send :",e);
            }
            System.out.println("----------------- topic: " + topic + " message: " + message);
        }

        producer.close();
    }

    public static void main(String[] args) {
        String topic = "topic-test";
        KProducer kProducer = new KProducer(topic);
        kProducer.run();
    }
}
