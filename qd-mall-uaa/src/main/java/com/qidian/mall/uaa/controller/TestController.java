package com.qidian.mall.uaa.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求，只是为了测试下安全认证http-security 资源服务器的相关过滤请求是否放开，不需要认证
 */
@Api(tags = {"测试安全认证的相关配置"})
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    @RequestMapping("/v1")
    public String testHttpSecurity(){
        return "ssssssssssss";
    }
}
