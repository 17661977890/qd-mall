package com.qidian.mall.message.hystrix;

import com.aliyuncs.CommonResponse;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.message.request.SendSmsDTO;
import org.springframework.stereotype.Component;

/**
 * 熔断降级
 * @Author binsun
 * @Date
 * @Description
 */
@Component
public class AliyunSmsApiHystrix extends BaseMessageHystrix implements AliyunSmsApi {


    @Override
    public RestResponse<CommonResponse> sendSms(SendSmsDTO sendSmsDTO) {
        return getError();
    }
}
