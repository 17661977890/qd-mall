package com.qidian.mall.uaa.websecurityconfig.impl;

import com.central.base.exception.BusinessException;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.qidian.mall.user.api.SysUserApi;
import com.qidian.mall.user.entity.CustomUserDetails;
import com.qidian.mall.uaa.websecurityconfig.mobileprovider.MobileUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通过feign 调用用户中心接口 验证登录验证信息是否正确
 * 因为MobileUserDetailsService接口已经继承了 UserDetailsService接口
 * 所以这里可以一起重写多个验证方法loadUserByUsername、loadUserByMobile
 *
 * oauthcontroller 中的 authenticationManager.authenticate(token); ---- 因为我们重写了验证链机制的验证方法，在底层 会调用我们重写的3中验证方法
 * @author mall
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements MobileUserDetailsService,SocialUserDetailsService {
    @Resource
    private SysUserApi sysUserApi;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * additionalAuthenticationChecks  底层方法 有根据UsernamePasswordAuthenticationToken 对密码做校验，这里只需要查询用户是否存在就行了
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        RestResponse<CustomUserDetails> restResponse = sysUserApi.findByUsername(username);
        if (restResponse == null || restResponse.getBody() == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }
        if(ConstantUtil.USER_SERVICE_NOT_AVAILABLE.equals(restResponse.getHeader().getMessage())){
            throw new BusinessException(ConstantUtil.ERROR,ConstantUtil.USER_SERVICE_NOT_AVAILABLE);
        }
        return restResponse.getBody();
    }

    @Override
    public SocialUserDetails loadUserByUserId(String openId) {
        RestResponse<CustomUserDetails>  restResponse = sysUserApi.findByOpenId(openId);
        return  restResponse.getBody();
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        RestResponse<CustomUserDetails>  restResponse = sysUserApi.findByMobile(mobile);
        return restResponse.getBody();
    }


}
