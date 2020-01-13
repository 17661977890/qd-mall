package com.central.base.mvc;

import com.central.base.message.MessageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础base controller
 * @Author binsun
 * @Date 2020-01-13
 * @Description
 */
public class BaseController {
    @Autowired
    private MessageSourceService messageSourceService;

    protected String getMessage(String code){
        return messageSourceService.getMessage(code);
    }
}
