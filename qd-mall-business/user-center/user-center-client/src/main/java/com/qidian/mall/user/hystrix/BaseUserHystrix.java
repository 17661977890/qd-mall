package com.qidian.mall.user.hystrix;

import com.central.base.restparam.RestResponse;
import com.central.base.restparam.RestResponseHeader;
import com.central.base.util.ConstantUtil;
import com.qidian.mall.user.entity.CustomUserDetails;

/**
 * 熔断机制处理父类----针对用户token生成的几个feign接口调用
 * @Author 彬
 * @Date 2019/4/26
 */
public class BaseUserHystrix {

    /**
     * 返回body 需要是UserDetails 否则会报类型不匹配异常，虽然我们这里返回头消息是服务调用异常，
     * 但是因为token的底层校验，会抛出异常BadCredentialsException  我们在oauthController 中进行捕获输出用户名密码错误的信息
     * 所以这里 只是返回一个空的用户信息即可。
     * 如果想显示服务调用异常，需要在oauth处 UserDetailServiceImpl 进行用户校验时候判断头部信息抛出异常
     * @return
     */
    protected RestResponse getError(){
        RestResponse restResponse = new RestResponse();
        CustomUserDetails customUserDetails = new CustomUserDetails();
        restResponse.setHeader(new RestResponseHeader(ConstantUtil.ERROR,ConstantUtil.USER_SERVICE_NOT_AVAILABLE));
        restResponse.setBody(customUserDetails);
        return restResponse;
    }


    protected RestResponse getError2(){
        RestResponse restResponse = new RestResponse();
        restResponse.setHeader(new RestResponseHeader(ConstantUtil.ERROR,ConstantUtil.USER_SERVICE_NOT_AVAILABLE));
        restResponse.setBody(null);
        return restResponse;
    }

}
