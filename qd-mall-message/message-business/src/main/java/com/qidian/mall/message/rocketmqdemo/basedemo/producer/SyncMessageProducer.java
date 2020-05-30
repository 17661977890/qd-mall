package com.qidian.mall.message.rocketmqdemo.basedemo.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 发送同步消息 -- 消息准确抵达，时间不敏感场景
 * @author bin
 * @data 2020-05-18
 */
public class SyncMessageProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、启动producer
        producer.start();
        //循环测试创建发送消息
        for (int i = 0; i < 10; i++) {
            // 4、创建消息对象，指定主题topic、tag和消息体
            /**
             * 参数1：消息主题topic
             * 参数2：消息tag
             * 参数3：消息内容
             */
            Message message =new Message("base","Tag1",("Hello world"+i).getBytes());
            // 5、发送消息(获取其状态、消息id、消息接受队列id)
            SendResult result = producer.send(message);
            SendStatus status = result.getSendStatus();
            String msgId = result.getMsgId();
            int queueId = result.getMessageQueue().getQueueId();
            System.out.println("发送结果："+result+"发送状态："+status+"消息id："+msgId+"接收队列id："+queueId);

            // 线程睡眠1s 后在发送
            TimeUnit.SECONDS.sleep(1);
        }
        // 6、关闭生产者
        producer.shutdown();

    }
}
