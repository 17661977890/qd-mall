package com.qidian.mall.message;

import com.alibaba.fastjson.JSONObject;
import com.central.base.exception.BusinessException;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.response.SendSmsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MessageCenterApplication.class})
public class MessageCenterTest {
//    @Resource
//    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private AliyunSmsApi aliyunSmsApi;


    @Test
    public void testMqSend(){
//        rocketMQTemplate.convertAndSend("springboot-mq","hello spring boot rocketMq");
    }

    @Test
    public void test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","123456");
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        sendSmsDTO.setPhoneNumbers("17661977890");
        sendSmsDTO.setTemplateParam(jsonObject.toJSONString());
        sendSmsDTO.setBusinessType(1);
        RestResponse<SendSmsVo> restResponse = aliyunSmsApi.sendSms(sendSmsDTO);
        log.info("invoke alibaba send sms api,result:{} ",restResponse);
        if(ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE.equals(restResponse.getHeader().getMessage())){
            throw new BusinessException(ConstantUtil.ERROR,ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE);
        }

    }
}
