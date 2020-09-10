package com.qidian.mall.message.hystrix;

import com.central.base.restparam.RestResponse;
import com.central.base.restparam.RestResponseHeader;
import com.central.base.util.ConstantUtil;

/**
 * 熔断机制处理父类
 * @Author 彬
 * @Date 2019/4/26
 */
public class BaseMessageHystrix {


    protected RestResponse getError(){
        RestResponse restResponse = new RestResponse();
        restResponse.setHeader(new RestResponseHeader(ConstantUtil.ERROR,ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE));
        restResponse.setBody(null);
        return restResponse;
    }

}
