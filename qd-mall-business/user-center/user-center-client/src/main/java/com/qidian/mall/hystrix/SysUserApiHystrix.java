package com.qidian.mall.hystrix;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.api.SysUserApi;

/**
 * @Author binsun
 * @Date
 * @Description
 */
public class SysUserApiHystrix extends BaseHystrix implements SysUserApi {
    @Override
    public RestResponse findByUsername(String username) {
        return getError();
    }

    @Override
    public RestResponse findByMobile(String mobile) {
        return getError();
    }

    @Override
    public RestResponse findByOpenId(String openId) {
        return getError();
    }
}
