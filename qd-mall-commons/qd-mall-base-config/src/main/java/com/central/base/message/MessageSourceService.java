package com.central.base.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * 国际化调用业务类
 * @Author 彬
 * @Date 2019/4/23
 */
@Service
public class MessageSourceService {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code){
        //locale 参数为区域参数，如果为null，就会去默认的messages.properties中找对应的key
        return messageSource.getMessage(code,null,null,null);
    }
}
