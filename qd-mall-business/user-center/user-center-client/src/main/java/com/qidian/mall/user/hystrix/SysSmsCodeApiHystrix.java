package com.qidian.mall.user.hystrix;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.api.SysSmsCodeApi;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import org.springframework.stereotype.Component;

/**
 * @Author binsun
 * @Date
 * @Description
 */
@Component
public class SysSmsCodeApiHystrix extends BaseUserHystrix implements SysSmsCodeApi {
    @Override
    public RestResponse<Boolean> verifyCode(SysSmsCodeDTO sysSmsCodeDTO) {
        return getError2();
    }
}
