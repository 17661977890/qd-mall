package com.qidian.mall.message.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用阿里云短信服务 返回结果参数
 */
@Data
public class SendSmsVo implements Serializable {
    private static final long serialVersionUID = 7722185756083066496L;

    private String message;

    private String requestId;

    private String code;

    private String bizId;
}
