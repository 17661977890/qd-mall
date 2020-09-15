package com.central.base.mvc;

import com.central.base.message.MessageSourceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础base serviceImpl
 * @Author binsun
 * @Date 2020-01-13
 * @Description
 */
public class BaseServiceImpl {
    @Autowired
    private MessageSourceService messageSourceService;

    protected String getMessage(String code){
        return messageSourceService.getMessage(code);
    }
}
