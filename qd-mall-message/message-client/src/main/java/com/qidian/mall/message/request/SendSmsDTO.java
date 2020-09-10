package com.qidian.mall.message.request;


import com.central.base.valid.EnumValue;
import com.qidian.mall.message.enums.SmsTemplateTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发送短信入参
 */
@Data
public class SendSmsDTO implements Serializable {

    private static final long serialVersionUID = 4231821504572232599L;

    /**
     * 手机号--- 支持多个，逗号分割即可 上限1000
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumbers;

    /**
     * 模板参数 格式：123456
     */
    @NotBlank(message = "短信模版参数不能为空")
    private String templateParam;


    /**
     * 阿里云模板管理code (对应阿里云的配置模版编号)
     */
    private String templateCode;

    /**
     * 业务类型：1 注册 2 登录 3 重置密码 等，对应不同的短信模版
     */
    @EnumValue(enumClass = SmsTemplateTypeEnum.class, enumMethod = "enumValueValid", message = "短信业务类型错误（注册：1，登录：2，重置密码：3）")
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

}
