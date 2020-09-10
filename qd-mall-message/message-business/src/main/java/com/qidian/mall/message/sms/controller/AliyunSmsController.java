package com.qidian.mall.message.sms.controller;


import com.aliyuncs.CommonResponse;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.message.enums.SmsTemplateTypeEnum;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.request.SmsQueryDTO;
import com.qidian.mall.message.response.SendSmsVo;
import com.qidian.mall.message.sms.service.AliyunSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sms-server")
@Api(tags = {"阿里云短信web"})
public class AliyunSmsController extends BaseController {

    @Autowired
    private AliyunSmsService aliyunSmsService;


    /**
     * 发送短信
     * @param sendSmsDTO
     * @return
     */
    @ApiOperation(value = "发送短信", notes = "阿里云发送短信，支持多手机")
    @RequestMapping(value = "/sendSms",method = RequestMethod.POST)
    public RestResponse<SendSmsVo> sendSms(@Validated @RequestBody SendSmsDTO sendSmsDTO){
        sendSmsDTO.setTemplateCode(SmsTemplateTypeEnum.getTemplateCodeByCode(sendSmsDTO.getBusinessType()));
        SendSmsVo sendSmsVo = aliyunSmsService.sendSms(sendSmsDTO);
        return new RestResponse<SendSmsVo>().success(sendSmsVo);
    }


    /**
     * 获取短信发送记录
     * @param smsQueryDTO
     * @return
     */
    @ApiOperation(value = "获取短信发送记录", notes = "获取阿里云发送短信记录")
    @RequestMapping(value = "/querySendDetails",method = RequestMethod.POST)
    public RestResponse<CommonResponse> querySendDetails(@Validated @RequestBody SmsQueryDTO smsQueryDTO){
        CommonResponse commonResponse = aliyunSmsService.querySendDetails(smsQueryDTO);
        return new RestResponse<CommonResponse>().success(commonResponse);
    }
}
