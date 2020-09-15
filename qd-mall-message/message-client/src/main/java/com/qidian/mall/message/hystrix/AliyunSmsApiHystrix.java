package com.qidian.mall.message.hystrix;

import com.aliyuncs.CommonResponse;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.response.SendSmsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 熔断降级
 * @Author binsun
 * @Date
 * @Description
 */
@Slf4j
@Component
public class AliyunSmsApiHystrix extends BaseMessageHystrix implements AliyunSmsApi {


    @Override
    public RestResponse<SendSmsVo> sendSms(SendSmsDTO sendSmsDTO) {
        log.error("调用消息服务失败，触发熔断。。。。");
        return getError();
    }
}
