package com.qidian.mall.message;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MessageCenterApplication.class})
public class MessageCenterTest {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testMqSend(){
        rocketMQTemplate.convertAndSend("springboot-mq","hello spring boot rocketMq");
    }
}
