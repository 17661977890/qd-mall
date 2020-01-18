package com.qidian.mall.hystrix;

import com.central.base.restparam.RestResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 熔断机制处理父类
 * @Author 彬
 * @Date 2019/4/26
 */
public class BaseHystrix {

    protected RestResponse getError(){
        RestResponse restResponse = new RestResponse();
        Map map = new HashMap();
        map.put("code",500);
        map.put("msg","服务调用异常");
        restResponse.setBody(map);
        return restResponse;
    }

}
