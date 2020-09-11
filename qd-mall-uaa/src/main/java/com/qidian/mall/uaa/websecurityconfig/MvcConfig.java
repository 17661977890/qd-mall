package com.qidian.mall.uaa.websecurityconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/login").setViewName("login"); //自定义的登陆页面
        registry.addViewController("/oauth/confirm_access").setViewName("oauth_approval"); //自定义的授权页面
    }
}