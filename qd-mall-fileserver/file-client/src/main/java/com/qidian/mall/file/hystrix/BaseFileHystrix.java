package com.qidian.mall.file.hystrix;

import com.central.base.restparam.RestResponse;
import com.central.base.restparam.RestResponseHeader;
import com.central.base.util.ConstantUtil;

/**
 * 熔断基础处理类
 * @author bin
 */
public class BaseFileHystrix {

    protected RestResponse getError(){
        RestResponse restResponse = new RestResponse();
        restResponse.setHeader(new RestResponseHeader(ConstantUtil.ERROR,ConstantUtil.FILE_SERVICE_NOT_AVAILABLE));
        restResponse.setBody(null);
        return restResponse;
    }
}
