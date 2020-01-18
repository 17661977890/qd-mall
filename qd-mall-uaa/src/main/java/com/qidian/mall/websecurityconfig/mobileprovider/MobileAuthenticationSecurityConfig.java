package com.qidian.mall.websecurityconfig.mobileprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * mobile的相关处理配置
 * 参考底层 AbstractDaoAuthenticationConfigurer和 DaoAuthenticationProvider 类实现
 *
 * @author sunbin
 */
@Component
public class MobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    // mobile provide
    private MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
    @Autowired
    private MobileUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity http) {
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        // authenticationProvider（） 此方法，不管是调用HttpSecurity的，还是直接调用 AuthenticationManagerBuilder 的
        // 最终都是将 此 provider 身份验证机制 追加到 AuthenticationManagerBuilder 的属性List<AuthenticationProvider> 认证机制链集合中
        http.authenticationProvider(provider);
    }
}
