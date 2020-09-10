package com.qidian.mall.message.mq.rocketmqdemo.batchdemo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息生产者
 * @author bin
 */
public class Producer {

    /**
     * 批量发送显著提高传递小消息的性能，限制是这些批量消息应该有相同的topic，相同的waitstoremsgok，而且不能是延时消息。此外
     * 这一批消息的总大小不超过4MB，如果超过，需要进行消息分割。（消息遍历，大小限制分割发送）
     *
     */
    public static void main(String[] args) throws Exception{
        // 1、创建生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、启动producer
        producer.start();

        // 4、构建消息对象(集合)，创建topic和tag
        List<Message> messageList = new ArrayList<>();
        Message message =new Message("BatchTopic","batch",("批量消息:"+1).getBytes());
        Message message1 =new Message("BatchTopic","batch",("批量消息:"+2).getBytes());
        Message message2 =new Message("BatchTopic","batch",("批量消息:"+3).getBytes());
        messageList.add(message);
        messageList.add(message1);
        messageList.add(message2);
        // 5、发送消息
        SendResult result = producer.send(messageList);
        System.out.println("发送结果："+result);
        // 6、关闭
        producer.shutdown();

    }
}
