package com.qidian.mall.message.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.central.base.exception.BusinessException;
import com.central.base.message.MessageSourceService;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.request.SmsQueryDTO;
import com.qidian.mall.message.sms.config.AliyunSMSConfig;
import com.qidian.mall.message.sms.service.AliyunSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Slf4j
@Service
public class AliyunSmsServiceImpl implements AliyunSmsService {

    @Autowired
    private AliyunSMSConfig aliyunSMSConfig;

    @Autowired
    private MessageSourceService messageSourceService;


    /**
     * 短信发送接口
     * @param sendSmsDTO
     * @return
     */
    @Override
    public CommonResponse sendSms(SendSmsDTO sendSmsDTO) {
        IAcsClient iAcsClient = getClient();
        CommonRequest commonRequest = getSendSmsRequest(sendSmsDTO);
        CommonResponse commonResponse =null;
        try {
            commonResponse = iAcsClient.getCommonResponse(commonRequest);
        } catch (ClientException e) {
            e.printStackTrace();
            log.error("发送短信发生错误。错误代码是 [{}]，错误消息是 [{}]，错误请求ID是 [{}]，错误Msg是 [{}]，错误类型是 [{}]",
                    e.getErrCode(),
                    e.getMessage(),
                    e.getRequestId(),
                    e.getErrMsg(),
                    e.getErrorType());
            throw new BusinessException("300001",messageSourceService.getMessage("300001"));
        }
        return commonResponse;
    }

    /**
     * 短信发送记录查询接口
     * @param smsQueryDTO
     * @return
     */
    @Override
    public CommonResponse querySendDetails(SmsQueryDTO smsQueryDTO) {
        IAcsClient iAcsClient = getClient();
        CommonRequest commonRequest = getSmsQueryRequest(smsQueryDTO);
        CommonResponse commonResponse =null;
        try {
            commonResponse = iAcsClient.getCommonResponse(commonRequest);
        } catch (ClientException e) {
            e.printStackTrace();
            log.error("查询发送短信记录发生错误。错误代码是 [{}]，错误消息是 [{}]，错误请求ID是 [{}]，错误Msg是 [{}]，错误类型是 [{}]",
                    e.getErrCode(),
                    e.getMessage(),
                    e.getRequestId(),
                    e.getErrMsg(),
                    e.getErrorType());
            throw new BusinessException("300002",messageSourceService.getMessage("300002"));
        }
        return commonResponse;
    }


    /**
     * 封装查询发送记录请求对象
     * @param smsQueryDTO:
     */
    private CommonRequest getSmsQueryRequest(SmsQueryDTO smsQueryDTO) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysVersion("2017-05-25");
        request.setSysDomain(aliyunSMSConfig.getDomain());
        request.setSysAction("QuerySendDetails");
        request.putQueryParameter("RegionId", aliyunSMSConfig.getRegionId());
        request.putQueryParameter("PhoneNumber", smsQueryDTO.getPhoneNumber());
        SimpleDateFormat ft = new SimpleDateFormat(aliyunSMSConfig.getDateFormat());
        request.putQueryParameter("SendDate", ft.format(smsQueryDTO.getSendDate()));
        request.putQueryParameter("PageSize", smsQueryDTO.getPageSize());
        request.putQueryParameter("CurrentPage", smsQueryDTO.getCurrentPage());
        return request;
    }


    /**
     * 封装发送短信请求对象
     * @param sendSmsDTO: 短信发送实体
     */
    private CommonRequest getSendSmsRequest(SendSmsDTO sendSmsDTO) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliyunSMSConfig.getDomain());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", aliyunSMSConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", sendSmsDTO.getPhoneNumbers());
        request.putQueryParameter("SignName", aliyunSMSConfig.getSignName());
        request.putQueryParameter("TemplateCode",sendSmsDTO.getTemplateCode() );
        request.putQueryParameter("TemplateParam", sendSmsDTO.getTemplateParam());
        return request;
    }

    /**
     * @Description: 获取短信发送服务机
     */
    private IAcsClient getClient() {

        IClientProfile profile = DefaultProfile.getProfile(aliyunSMSConfig.getRegionId(),
                aliyunSMSConfig.getAccessKeyId(),
                aliyunSMSConfig.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
