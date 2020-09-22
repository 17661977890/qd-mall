package com.qidian.mall.user.hystrix;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.api.SysUserApi;
import com.qidian.mall.user.entity.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * 熔断降级
 * @Author binsun
 * @Date
 * @Description
 */
@Component
public class SysUserApiHystrix extends BaseUserHystrix implements SysUserApi {
    @Override
    public RestResponse queryByUsername(String username) {
        return getError2();
    }

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
