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


    /**
     * @Description: 将数据转换到相应的容器
     * @param bean
     * @param clazz
     * @return
     * @throws
     * @author SunF
     * @date 2018/6/20 10:28
     */
    public static <T> T convertValue(Object bean, Class<T> clazz){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(bean, clazz);
        }catch(Exception e){
            log.error("错误的转换：BeanUtil.convertValue() --->" + e.getMessage());
            return null;
        }
    }

}
