package com.qidian.mall.message.mq.rocketmqdemo.basedemo.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 发送异步消息 -- 消息不一定准确发送，时间敏感场景
 * @author bin
 * @data 2020-05-18
 */
public class AsyncMessageProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、启动producer
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //循环测试创建发送消息
        for (int i = 0; i < 10; i++) {
            // 4、创建消息对象，指定主题topic、tag和消息体
            /**
             * 参数1：消息主题topic
             * 参数2：消息tag
             * 参数3：消息内容
             */
            Message message =new Message("base","Tag2",("Hello world"+i).getBytes());
            // 5、发送异步消息(利用回调函数获取结果)
            producer.send(message, new SendCallback() {
                /**
                 * 发送成功
                 * @param sendResult
                 */
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送结果："+sendResult);
                }

                /**
                 * 发送异常失败
                 * @param throwable
                 */
                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送异常："+throwable);
                }
            });

            // 线程睡眠1s 后在发送
            TimeUnit.SECONDS.sleep(1);
        }
        // 6、关闭生产者
        producer.shutdown();

    }
}
