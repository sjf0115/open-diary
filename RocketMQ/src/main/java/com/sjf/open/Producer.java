package com.sjf.open;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * Created by xiaosi on 16-7-2.
 */
public class Producer {

    private static String PRODUCER_GROUP_NAME = "RocketMQProducerGroup";
    private static String PRODUCER_NAME = "RocketMQProducerOne";
    private static String ip = "192.168.0.107";//"10.86.14.23";
    private static String port = "9876";

    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    /**
     * 创建生产者
     * @return
     */
    public static DefaultMQProducer createProducer(){
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP_NAME);
        producer.setNamesrvAddr(ip + ":" + port);
        producer.setInstanceName(PRODUCER_NAME);
        return producer;
    }

    /**
     * 启动生产者
     * @param producer
     * @return
     */
    public static boolean start(DefaultMQProducer producer){
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("RocketMQ 生产者启动 失败 {}",e);
            return false;
        }
        return true;
    }

    /**
     * 生产者发送消息
     * @param producer
     */
    public static void send(DefaultMQProducer producer){
        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
         * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br>
         * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br>
         * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
         */
        for (int i = 0; i < 50; i++) {
            try {
                {
                    Message msg = new Message("TopicTest1",// topic
                            "TagA",// tag
                            "OrderID001",// key
                            ("Hello MetaQ").getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest2",// topic
                            "TagB",// tag
                            "OrderID0034",// key
                            ("Hello MetaQ").getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest3",// topic
                            "TagC",// tag
                            "OrderID061",// key
                            ("Hello MetaQ").getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生产者退出
     * @param producer
     */
    public static void close(DefaultMQProducer producer){
        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();
    }
    public static void main(String[] args) throws MQClientException,
            InterruptedException {

        // 创建生产者
        DefaultMQProducer producer = createProducer();
        // 启动
        boolean result = start(producer);
        if(!result){
            return;
        }
        // 发送消息
        send(producer);
        // 关闭
        close(producer);
    }
}
