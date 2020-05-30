package com.qidian.mall.message.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * springboot整合 消费者 消息监听 案例（一般会在其他需要的业务模块使用，这里仅供案例参考）
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "springboot-mq",consumerGroup = "my-group")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("消费者监听消息："+message);
    }
}
