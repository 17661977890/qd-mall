package com.qidian.mall.uaa.websecurityconfig;

import com.central.auth.common.errorresponse.RestAuthenticationEntryPoint;
import com.central.auth.common.errorresponse.RestfulAccessDeniedHandler;
import com.qidian.mall.uaa.websecurityconfig.mobileprovider.MobileAuthenticationSecurityConfig;
import com.qidian.mall.uaa.websecurityconfig.openIdprovider.OpenIdAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;


/**
 * spring security相关的主要类：-----身份验证和授权
 *
 * 可见的几个类：
 * 1.WebSecurityConfigurerAdapter
 * 2.HttpSecurity
 * 3.AuthenticationManagerBuilder
 *
 * WebSecurityConfigurerAdapter是默认情况下spring security的http配置：
 * 在ResourceServerProperties中，定义了它的order默认值为SecurityProperties.ACCESS_OVERRIDE_ORDER - 1;，
 * 是大于100的,即WebSecurityConfigurerAdapter的配置的拦截要优先于ResourceServerConfigurerAdapter，
 * 优先级高的http配置是可以覆盖优先级低的配置的。
 * 某些情况下如果需要ResourceServerConfigurerAdapter的拦截优先于WebSecurityConfigurerAdapter
 * 优先级高于ResourceServerConfigurer，用于保护oauth相关的endpoints，同时主要作用于用户的登录（form login，Basic auth）
 * 需要在配置文件中添加 security.oauth2.resource.filter-order=99 或者重写WebSecurityConfigurerAdapter的Order配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;
    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired(required = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private LogoutHandler oauthLogoutHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    /**
     * （1）全局用户认证身份管理器
     *  用户信息的全局配置（全局资源：为所有程序提供登录验证后备，登陆时的输入信息以此为准）
     *  注入configureGlobal方法，方法名无所谓，通过userDetailsService类 来获取用户数据--------底层默认就是用户名密码进行登录验证
     *
     *  顶层接口：SecurityBuilder
     *  用于创建一个AuthenticationManager。 允许轻松构建内存验证，LDAP身份验证，基于JDBC的身份验证，
     *  添加UserDetailsService以及添加AuthenticationProvider。
     *
     *  AuthenticationManager的认证功能由最常用的实现者之一ProviderManager来实现，而ProviderManager没有做实事，
     *  而是将认证的实现委托给了一个List<AuthenticationProvider> providers。
     *
     *  ProviderManager可以通过委托给一个AuthenticationProviders链来支持同一应用程序中的多个不同的身份验证机制，
     *  而这个AuthenticationProviders链就是ProviderManager类中维护的List<AuthenticationProvider>。
     *  而这个不同的身份验证机制指的就是多种验证方式：用户名密码凭证登录、手机登录、指纹登录、甚至是IphoneX饱受吐槽的FaceID认证，
     *  每一种认证方式对应一个AuthenticationProviders。
     *
     *  如果传入的待认证凭据Authentication认证失败，会采用ProviderManager中List<AuthenticationProvider>的下一个AuthenticationProvider进行认证。
     *
     *
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // AbstractDaoAuthenticationConfigurer实例初始化了userDetailsService属性，
        // 以及为属性DaoAuthenticationProvider 设置 userDetailsService 属性，
        // 创建了UserBuilder对象，并设置了用户名和密码，
        // 所以再具体认证的时候，就会访问到这个userDetailsService，我们也可以对里面的方法进行重写来判断是否通过登录认证。
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * （2）设置获取token的url，即请求授权
     */

    /** 使用匹配器匹配路径:
     *  authorizeRequests()方法：开始请求权限配置，即定义那些url需要被保护，哪些不需要
     *  antMatchers:使用ant风格的路径进行匹配
     *  regexMatchers：使用正则表达式匹配路径
     *  anyRequest:匹配所有请求路径
     *  csrf().disable()：关闭csrf，防止请求出现csrf跨站请求伪造。
     *  authenticated()，为用户登陆后即可访问。
     *  permitAll(),不需要登录即可访问
     *  httpBasic()：Http Basic认证方式------这里未使用
     *      流程：1.浏览器发送get请求到服务器，服务器检查是否含有请求头Authorization信息，
     *          若没有则返回响应码401且加上响应头
     *          2.浏览器得到响应码自动弹出框让用户输入用户名和密码，
     *          3.浏览器将用户名、冒号和密码进行base64编码（用户名：密码 admin:admin YWRtaW4lM0FhZG1pbg==），设置请求头Authorization，继续访问
     *          Basic认证的优点是基本上所有流行的网页浏览器都支持，一般被用在受信赖的或安全性要求不高的系统中（如路由器配置页面的认证，tomcat管理界面认证）
     *          缺点是，用户名密码基本等于是明文传输带来很大风险，并且没有注销认证信息的手段，只能依靠关闭浏览器退出认证。
     * OPTIONS方法是用于请求获得由Request-URI标识的资源在请求/响应的通信过程中可以使用的功能选项。
     * 通过这个方法，客户端可以在采取具体资源请求之前，决定对该资源采取何种必要措施，或者了解服务器的性能。
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 由于使用的是JWT，我们这里不需要csrf,不使用session   // todo 不使用session 无状态应用 符合12要素
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                // 自定义登录页面（用户未授权请求资源会跳转此页面）
                .formLogin()
                    .loginPage("/login.html")
                    // 登录请求处理地址、成功返回、失败返回
                    .loginProcessingUrl("/user/token")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                // 登出配置
                .logout()
                    .logoutUrl("/remove/token")
                    .logoutSuccessUrl("/login.html")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .addLogoutHandler(oauthLogoutHandler)
                    .clearAuthentication(true)
                    .and()
                .apply(openIdAuthenticationSecurityConfig)
                    .and()
                .apply(mobileAuthenticationSecurityConfig)
                    .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")// 对登录注册要允许匿名访问，我们本项目暂时没有两个请求
                .permitAll()
                // 除以上请求 其余请求都要走认证---这里做了，那资源服务器的相关配置（要放开的请求）就不生效了，除了上面的配置都会拦截
//                .anyRequest()
//                .authenticated()
                    .and()
                // 自定义为授权和认证失败返回结果----因为我们在资源服务器对相关请求做的过滤拦截，那相关的异常信息也要在那边配置，这边不会生效
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restfulAccessDeniedHandler);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        //添加自定义未授权和未登录结果返回-------暂时不用
        // （1）基于密码 等模式可以无session,不支持授权码模式
//        if (authenticationEntryPoint != null) {
//            httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
//            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        } else {
//            // 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
//            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//        }
        // （2）授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
//        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }


    /**
     * 必不可少，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     * @return 认证管理对象
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}