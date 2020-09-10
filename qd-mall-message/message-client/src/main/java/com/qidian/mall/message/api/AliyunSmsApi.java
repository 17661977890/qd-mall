package com.qidian.mall.message.api;

import com.aliyuncs.CommonResponse;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.message.hystrix.AliyunSmsApiHystrix;
import com.qidian.mall.message.request.SendSmsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "qd-mall-messageserver",configuration = FeignClientsConfiguration.class,fallback = AliyunSmsApiHystrix.class)
public interface AliyunSmsApi {


    @RequestMapping(value = "/sms-server/sendSms",method = RequestMethod.POST)
    RestResponse<CommonResponse> sendSms(@Validated @RequestBody SendSmsDTO sendSmsDTO);
}
