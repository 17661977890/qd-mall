package com.qidian.mall.user.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 短信验证码发送结果出参
 * @Author binsun
 * @Date 2020-09-10
 * @Description
 */
@Data
public class SysSmsCodeVo implements Serializable {

    private static final long serialVersionUID = 5045096618576487040L;
    /**
     * 消息验证码id
     */
    private String smsCodeId;

    /**
     * 验证码
     */
    private String verificationCode;
}
