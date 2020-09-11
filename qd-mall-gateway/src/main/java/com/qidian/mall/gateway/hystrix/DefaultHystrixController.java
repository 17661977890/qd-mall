package com.qidian.mall.gateway.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 熔断处理类
 * @author bin
 */
@Slf4j
@RestController
public class DefaultHystrixController {

    @RequestMapping("/defaultfallback")
    public Map<String,String> defaultfallback(){
        log.error("======服务降级中=====");
        Map<String,String> map = new HashMap<>();
        map.put("code","500");
        map.put("msg","服务异常");
        map.put("body","null");
        return map;
    }
}
