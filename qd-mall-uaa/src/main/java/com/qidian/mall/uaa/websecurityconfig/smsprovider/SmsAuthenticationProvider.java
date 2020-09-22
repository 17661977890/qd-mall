package com.qidian.mall.uaa.websecurityconfig.smsprovider;

import com.qidian.mall.uaa.websecurityconfig.mobileprovider.MobileUserDetailsService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 手机认证--provider
 * @author sunbin
 */
@Setter
public class SmsAuthenticationProvider implements AuthenticationProvider {
    private MobileUserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        UserDetails user = userDetailsService.loadUserByMobile(mobile);
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号错误");
        }
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * 重写此方法目的：
     * 底层ProviderManager验证管理类中的验证方法 会遍历所有的定义的provider，并根据token的类型来选择那个provider验证机制。
     * 所以重写写这个方法就是为了 手机验证登录时 匹配到此provider 来进行业务逻辑的验证。
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
