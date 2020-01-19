package com.qidian.mall.websecurityconfig.impl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.central.base.restparam.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qidian.mall.api.SysUserApi;
import com.qidian.mall.entity.CustomUserDetails;
import com.qidian.mall.websecurityconfig.mobileprovider.MobileUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通过feign 调用用户中心接口 验证登录验证信息是否正确
 * 因为MobileUserDetailsService接口已经继承了 UserDetailsService接口
 * 所以这里可以一起重写多个验证方法loadUserByUsername、loadUserByMobile
 * @author mall
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements MobileUserDetailsService,SocialUserDetailsService {
    @Resource
    private SysUserApi sysUserApi;

    @Override
    public UserDetails loadUserByUsername(String username) {
        RestResponse<CustomUserDetails> restResponse = sysUserApi.findByUsername(username);
        if (restResponse == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }
        return restResponse.getBody();
    }

    @Override
    public SocialUserDetails loadUserByUserId(String openId) {
        RestResponse restResponse = sysUserApi.findByOpenId(openId);
        return  (CustomUserDetails)restResponse.getBody();
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        RestResponse restResponse = sysUserApi.findByMobile(mobile);
        return  (CustomUserDetails)restResponse.getBody();
    }


}
