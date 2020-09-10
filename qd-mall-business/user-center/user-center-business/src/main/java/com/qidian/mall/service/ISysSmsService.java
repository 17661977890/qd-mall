package com.qidian.mall.service;

import com.qidian.mall.request.SysSmsCodeDTO;
import com.qidian.mall.response.SysSmsCodeVo;

/**
 * 短信发送服务
 * @Author binsun
 * @Date 2020-09-10
 * @Description
 */
public interface ISysSmsService {


    /**
     * 发送短信验证码
     * @param sysSmsCodeDTO
     * @return
     */
    SysSmsCodeVo sendSmsVerificationCode(SysSmsCodeDTO sysSmsCodeDTO);
}
