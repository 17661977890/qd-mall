package com.qidian.mall.message.sms.service;

import com.aliyuncs.CommonResponse;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.request.SmsQueryDTO;
import com.qidian.mall.message.response.SendSmsVo;

public interface AliyunSmsService  {

    /**
     * 短信发送接口
     * @param sendSmsDTO
     * @return
     */
    SendSmsVo sendSms(SendSmsDTO sendSmsDTO);


    /**
     * 短信查询发送记录接口
     * @param smsQueryDTO
     * @return
     */
    CommonResponse querySendDetails(SmsQueryDTO smsQueryDTO);
}
