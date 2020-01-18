package com.qidian.mall.websecurityconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务器配置：
 * 要访问资源服务器受保护的资源需要携带令牌（从授权服务器获得）
 * ResourceServerConfigurerAdapter是默认情况下spring security oauth2的http配置
 *
 * 打断点测试：加载顺序优先于WebSecurityConfiguration
 **/
@Configuration
@EnableResourceServer //注解来开启一个 OAuth2AuthenticationProcessingFilter 类型的过滤器
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
