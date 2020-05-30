package com.qidian.mall.message.rocketmq.orderdemo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 消费者 : 消费顺序消息
 * @author bin
 */
public class Consumer {

    /**
     * 消费消息
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 1、创建消费者Consumer，制定消费者组名（push broker 去推给消费者，还有拉的消费者pull）
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        // 2、指定nameserver地址
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、订阅主题Topic和tag （切换tag，消费同步 异步 单向消息） 消费多个tag：  Tag1 ｜｜ Tag2 ｜｜ Tag3  直接写 * 消费所有的tag
        consumer.subscribe("OrderTopic","*");

        // 4、注册消息监听器，消费顺序消息： MessageListenerOrderly
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt messageExt : list) {
                    System.out.println("消费消息："+new String(messageExt.getBody())+"  Thread-name："+Thread.currentThread().getName());
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 5、启动消费者consumer
        consumer.start();
    }
}
