package com.qidian.mall.message.rocketmq.tarnsationdemo;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.TimeUnit;

/**
 * 事务消息生产者 ------ 看生产者打印结果和消费者的消费情况可看出 事务消息的执行流程
 * @author bin
 */
public class Producer {

    /**
     *  生产者发送（半消息）-- mq服务器返回半消息发送成功结果 --- 执行本地事务 --- 发送方告知消息服务器 事务状态：消息提交或者rollback
     *  ----如果提交，就可以被消费，
     *  ----回滚就删除消息，不会被消费
     *  ----如果（发送方）告知服务器（不知道是提交还是回滚）失败了---- mq服务器会发起事务消息的补偿流程，即对生产者发起回查。回查来获取消息的状态
     *
     * 事务消息3个状态：
     *  提交状态：允许消费
     *  回滚状态：消息删除，不允许消费
     *  中间状态：代表需要消息队列来确定状态
     */
    public static void main(String[] args) throws Exception{
        // 1、创建事务消息生产者producer，并制定生产者组名
        TransactionMQProducer producer = new TransactionMQProducer("group2");
        // 2、指定nameserver地址(如果是集群，用分号隔开 192.168.0.110:9876;192.168.0.111:9876)
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.111:9876");
        // 6、添加事务监听器
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 在该方法中执行本地事务
             * @param message
             * @param o
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                if(StringUtils.equals("Tag1",message.getTags())){
                    // 提交消息事务
                    return LocalTransactionState.COMMIT_MESSAGE;
                }else if(StringUtils.equals("Tag2",message.getTags())){
                    // 回滚消息事务
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }else if(StringUtils.equals("Tag3",message.getTags())){
                    // 不做处理
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.UNKNOW;
            }

            /**
             * 该方法是mq 进行消息事务状态的回查 （消息发送方没有告知消息mq服务器消息事务的状态是提交还是回滚，服务器发起回查事务补偿）
             * @param messageExt
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                // 回查提交
                System.out.println("事务消息状态没有返回，消息回查："+messageExt.getTags());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        // 3、启动producer
        producer.start();
        String[] tags = {"Tag1","Tag2","Tag3"};
        //循环测试创建发送消息
        for (int i = 0; i < 3; i++) {
            // 4、创建消息对象，指定主题topic、tag和消息体
            /**
             * 参数1：消息主题topic
             * 参数2：消息tag
             * 参数3：消息内容
             */
            Message message =new Message("TransactionTopic",tags[i],("Hello world"+i).getBytes());

            // 5、事务控制下发送half消息 （可以将事务控制应用某个消息，或者是整个producer，下面传null 是对整个producer应用事务控制）
            SendResult result = producer.sendMessageInTransaction(message,null);

            System.out.println("发送结果："+result);

            // 线程睡眠1s 后在发送
            TimeUnit.SECONDS.sleep(1);
        }
        // 6、关闭生产者 -- 因为要回查 所以不要关闭
//        producer.shutdown();

    }
}
