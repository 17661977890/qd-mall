package com.qidian.mall.uaa.websecurityconfig;


import com.central.auth.common.errorresponse.RestAuthenticationEntryPoint;
import com.central.auth.common.errorresponse.RestfulAccessDeniedHandler;
import com.central.auth.common.properties.SecurityProperties;
import com.qidian.mall.uaa.tokenstore.config.TokenStoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 资源服务器配置---我们将认证服务器和资源服务器放在一起配置，
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
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /**
     * 此处配置在WebSecurityConfiguration之后执行，拦截所有请求，除了我们在PermitProperties此类和配置文件中配置的需要放开认证的请求匹配路径，
     * 其余请求都需要登录认证后，拿到token后才可以访问。
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable()
                    .and()
                // 这里 要配置为true，否则不会匹配请求，就不会拦截了，如果返回是true表示提供的请求与提供的匹配规则匹配，如果返回的是false则不匹配。
                .requestMatcher(request -> true)
                .authorizeRequests()
                .antMatchers(securityProperties.getIgnore().getUrls()).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // 自定义为授权和认证失败返回结果
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restfulAccessDeniedHandler);;
    }
}
