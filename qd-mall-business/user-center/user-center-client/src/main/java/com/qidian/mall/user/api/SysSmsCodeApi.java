package com.qidian.mall.user.api;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.entity.CustomUserDetails;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.hystrix.SysSmsCodeApiHystrix;
import com.qidian.mall.user.hystrix.SysUserApiHystrix;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * feign 接口的url和web端的url要保持一致，否则调用feign接口的时候404，无法映射到web端
 *
 * 或者使用另外一个写法，单独抽一个公共api接口层，让生产者实现此接口，在此实现类实现相应业务逻辑，就不用url保持一致来绑定。
 * @Author binsun
 * @Date 2020-01-18
 * @Description
 */
@FeignClient(value = "qd-mall-usercenter",configuration = FeignClientsConfiguration.class,fallback = SysSmsCodeApiHystrix.class)
public interface SysSmsCodeApi {


    /**
     * ================== uaa 获取用户信息使用 ==============
     */

    @RequestMapping(value = "/sys-sms-code/verifyCode",method = RequestMethod.POST)
    RestResponse<Boolean> verifyCode(@RequestBody SysSmsCodeDTO sysSmsCodeDTO);


}
