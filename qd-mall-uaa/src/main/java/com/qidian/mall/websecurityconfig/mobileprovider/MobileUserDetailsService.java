package com.qidian.mall.websecurityconfig.mobileprovider;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author sunbin
 * @date 2018/12/28
 */
public interface MobileUserDetailsService extends UserDetailsService {
    /**
     * 根据电话号码查询用户-------业务接口实现此方法
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);
}
