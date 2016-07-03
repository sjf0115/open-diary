package com.sjf.open;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Objects;

/**
 * Created by xiaosi on 16-7-2.
 */

/**
 * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。<br>
 * 但是实际PushConsumer内部是使用长轮询Pull方式从MetaQ服务器拉消息，然后再回调用户Listener方法<br>
 */

public class Consumer {

    private static String CONSUMER_GROUP_NAME = "RocketMQConsumerGroup";
    private static String CONSUMER_NAME = "RocketMQConsumerOne";
    private static String ip = "192.168.0.107";// "10.86.14.23";
    private static String port = "9876";

    private static String topicOne = "TopicTest1";
    private static String topicTwo = "TopicTest2";

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
     * 注意：ConsumerGroupName需要由应用来保证唯一
     */
    private static DefaultMQPushConsumer createConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
        consumer.setNamesrvAddr(ip + ":" + port);
        consumer.setInstanceName(CONSUMER_NAME);
        logger.info("createConsumer---");
        return consumer;
    }

    /**
     * 订阅 注意：一个consumer对象可以订阅多个topic
     * 
     * @param consumer
     */
    private static void subscrible(DefaultMQPushConsumer consumer) {
        try {
            // 订阅指定topic下tags分别等于TagA或TagC或TagD
            consumer.subscribe(topicOne, "TagA || TagC || TagD");
            // 订阅指定topic下所有消息
            consumer.subscribe(topicTwo, "*");
            logger.error("subscrible 订阅成功");
        } catch (MQClientException e) {
            logger.error("subscrible 订阅失败 {}", e);
        }
    }

    /**
     * 处理消息
     * @param messageList
     */
    private static void consumeSubscribleMessage(List<MessageExt> messageList){
        int size = messageList.size();
        MessageExt message;
        logger.info("订阅的消息个数 {}",size);
        for(int i = 0;i < size;i++){
            message = messageList.get(i);
            if (Objects.equal(message.getTopic(),topicOne)) {
                String tag = message.getTags();
                if(Objects.equal(tag,"TagA")){
                    logger.info("TagA ---- {}",message.getBody());
                }
                else{
                    logger.info("其他 {} --- {}",tag, message.getBody());
                }
            } else if (Objects.equal(message.getTopic(),topicTwo)) {
                logger.info("主题二 {} ",message.getBody());
            }
        }//for
    }

    /**
     * 消息监听器 订阅处理消息
     * 
     * @param consumer
     */
    private static void registerMessageListener(DefaultMQPushConsumer consumer) {
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 默认messageList里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageList, ConsumeConcurrentlyContext context) {
                logger.info("{} Receive New Messages {}", Thread.currentThread().getName(), messageList.size());
                consumeSubscribleMessage(messageList);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }

    /**
     * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
     * 
     * @param consumer
     */
    private static void start(DefaultMQPushConsumer consumer) {
        try {
            consumer.start();
            logger.info("消费者启动成功");
        } catch (MQClientException e) {
            logger.error("消费者启动失败 {}", e);
        }
    }

    public static void main(String[] args) {
        DefaultMQPushConsumer consumer = createConsumer();
        subscrible(consumer);
        registerMessageListener(consumer);
        start(consumer);
    }
}
