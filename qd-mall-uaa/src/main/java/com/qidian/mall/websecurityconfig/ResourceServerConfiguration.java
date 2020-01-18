package com.qidian.mall.websecurityconfig;


import com.qidian.mall.tokenstore.config.TokenStoreConfig;
import com.qidian.mall.utils.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 资源服务器配置
 * @EnableResourceServer 注解来开启一个 OAuth2AuthenticationProcessingFilter 类型的过滤器
 * @EnableConfigurationProperties(SecurityProperties.class) 此注解配合 @ConfigurationProperties(prefix = "mall.security") 使用，确保可以注入 spring可以扫描获取属性
 * @author mall
 * @date 2018/10/27
 */
@EnableConfigurationProperties(SecurityProperties.class)
@Configuration
@EnableResourceServer
@Import(TokenStoreConfig.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private SecurityProperties securityProperties;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable()
                    .and()
                .requestMatcher(request -> false)
                .authorizeRequests()
                .antMatchers(securityProperties.getIgnore().getUrls()).permitAll()
                .anyRequest()
                .authenticated();
    }
}
