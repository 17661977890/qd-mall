package com.qidian.mall.message.rocketmqdemo.delaydemo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * 延迟消息生产者
 * @author bin
 */
public class Producer {

    /**
     * 使用限制： 延迟时间是约定的：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * 现在rocketMq 并不支持任意时间的延时，需要设置几个固定的延时等级，从1s-2h 一共18个等级
     *
     */
    public static void main(String[] args) throws Exception{
        // 1、创建生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、启动producer
        producer.start();

        // 4、发送消息（将订单一致的消息发送到同一个队列）
        for (int i=0;i<10;i++) {
            // 5、构建消息对象，创建topic和tag
            Message message =new Message("DelayTopic","delay","i"+i,("延时消息:"+i).getBytes());
            // 6、设置延时等级为3 即消息在10s以后发送消息
            message.setDelayTimeLevel(3);
            SendResult result = producer.send(message);
            System.out.println("发送结果："+result);
        }

        // 6、关闭
        producer.shutdown();

    }
}
