package com.qidian.mall.message.mq.rocketmqdemo.orderdemo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * 顺序消息生产者
 * @author bin
 */
public class Producer {

    public static void main(String[] args) throws Exception{
        // 1、创建生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 3、启动producer
        producer.start();
        // 构建消息集合
        List<Order> orderList = Order.buildOrders();
        // 4、发送消息（将订单一致的消息发送到同一个队列）
        for (int i=0;i<orderList.size();i++) {
            // 5、构建消息对象，创建topic和tag
            Message message =new Message("OrderTopic","Order","i"+i,("顺序消息:"+orderList.get(i)).getBytes());
            /**
             * 参数一：消息对象
             * 参数二：消息队列的选择器
             * 参数三：选择队列的业务标示（订单id）
             */
            SendResult result = producer.send(message, new MessageQueueSelector() {
                /**
                 * 队列选择器：
                 * @param list 队列集合
                 * @param message 消息对象
                 * @param o 业务标识的参数
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    long orderId = (long)o;
                    long index = orderId % list.size();
                    return list.get((int)index);
                }
            },orderList.get(i).getOrderId());

            System.out.println("发送结果："+result);
        }

        // 6、关闭
        producer.shutdown();

    }
}
