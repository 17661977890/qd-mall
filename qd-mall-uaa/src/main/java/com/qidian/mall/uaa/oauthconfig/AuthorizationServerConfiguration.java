package com.qidian.mall.uaa.oauthconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

/**
 * 一、配置oauth2授权认证服务机制
 *  主要是重写3个配置方法：
 *  （1）定义授权类型和令牌端点以及令牌服务
 *  （2）配置令牌端点(Token Endpoint)的安全约束
 *  （3）配置客户端详情服务（ClientDetailsService）
 * @author 彬
 */
@Configuration
@EnableAuthorizationServer //开启授权服务功能
@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器，当你选择了资源所有者密码------------------（password）授权类型，
     * 请设置这个属性注入一个 AuthenticationManager 对象
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 存储授权码（授权码管理）
     */
    @Autowired
    private RandomValueAuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * （1）定义授权类型和令牌端点以及令牌服务：（提供授权访问端点，token的生成存储方案）
     * 配置对象有一个 pathMapping() 方法用来配置端点的 URL
     * AuthorizationEndpoint 服务于认证请求。默认 URL： /oauth/authorize。
     * TokenEndpoint 服务于访问令牌的请求。默认 URL： /oauth/token。 ---------登录时先请求这个
     *
     *  1、通过注入 AuthenticationManager 密码授权
     *  2、userDetailsService：如果你注入一个 UserDetailsService，或者全局地配置了一个UserDetailsService
     *      （例如在 GlobalAuthenticationManagerConfigurer中），那么刷新令牌授权将包含对用户详细信息的检查，以确保该帐户仍然是活动的
     *  3、authorizationCodeServices：为授权代码授权定义授权代码服务（AuthorizationCodeServices 的实例）。
     *  4、implicitGrantService：在 imlpicit 授权期间管理状态。这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态
     *  5、tokenGranter：TokenGranter（完全控制授予和忽略上面的其他属性），完全自定义授权服务实现（TokenGranter 接口实现），只有当标准的四种授权模式已无法满足需求时
     *  6、accessTokenConverter： 指定jwt token令牌生成转换器（我们这里自定义）
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if (jwtAccessTokenConverter != null) {
            // 只有token存储配置为jwt时候，才会注入这个token生成转换器（追加用户信息）---tokenstore.jwt包中相关信息
            endpoints.accessTokenConverter(jwtAccessTokenConverter);
        }
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenStore(tokenStore);
    }

    /**
     * （2）配置令牌端点(Token Endpoint)的安全约束.创建ClientCredentialsTokenEndpointFilter核心过滤器
     * checkTokenAccess("permitAll()")：开启/oauth/check_token验证端口无权限访问
     * tokenKeyAccess("isAuthenticated()")：开启/oauth/token_key验证端口认证权限访问
     *
     * /oauth/token_key(如果使用JWT，可以获的公钥用于 token 的验签)
     * Public Key公布在/oauth/token_key这个URL连接中，默认的访问安全规则是"denyAll()"，即在默认的情况下它是关闭的，所以需要在这里配置开启或者权限访问
     *
     * 这个如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
     * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
     *
     * 第三行代码是允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401。
     * 第二行是允许已授权用户访问 获取 token 接口。
     * 一：不需要授权即可访问checkToken 接口
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //让/oauth/token支持client_id以及client_secret作登录认证
                .allowFormAuthenticationForClients();
    }

    /**
     * （3）配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
     * 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     * -----我们这里用的是调取数据库的方式  即通过JdbcClientDetailsService类访问oauth_client_details表，并实现类redis的缓存优化配置
     * 在oauth_client_details表中我们设置存活时间为100000秒，底层默认是12h，但会优先调取我们的设置。
     * 具体可看底层DefalutTokenService类
     *
     * 写死方式 ：clients.inMemory().withClient("user-service")// 创建一个客户端 名字是user-service
     *                 .secret("123456")
     *                 .scopes("service")
     *                 .authorizedGrantTypes("refresh_token", "password")
     *                 .accessTokenValiditySeconds(3600);
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * 客户端详情配置注入（实现方式）
     * 每个客户端在数据库都有自己设置的授权类型和token失效时间以及授权范围
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        return new ClientDetailsServiceImpl();
    }

    /**
     * 授权码管理方式注入（自行实现：redis管理授权码）
     * @return
     */
    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices() {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }

//==================================================下面是旧版 token的生成配置（不够灵活，单一性）======================================

    /**
     * token令牌生成转换器:
     * AuthorizationServerTokenServices 提供了对AccessToken的相关操作创建、刷新、获取
     * OAuth2就默认为我们提供了一个默认的DefaultTokenServices。包含了一些有用实现，------里面设置了默认的存活时间12H
     * 可以使用它来修改令牌的格式和令牌的存储等，但是生成的token是随机数。
     *
     * 这里使用继承使用Jwt的方式，添加额外属性，即转换器。
     * ------目前是对称密钥签署令牌
     * ------优化使用RSA非对称加密签署token
     * @return
     */
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        CustomJwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
//        //使用非对称加密RSA
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("ljl-jwt.jks"),
//                "ljl123".toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("ljl-jwt"));
//        log.info("================对token使用非对称加密RSA算法=======================");
    //使用对称加密
//        converter.setSigningKey("secret");
//        return converter;
//    }

    /**
     * token令牌存储器---------------------------tokenstore包下面扩展了集中token存储的方式，通过配置文件配置来获取具体的存储配置形式
     * 这里使用自定义redis缓存存储。
     * 也有其他多种实现方式
     * inMemoryTokenStore：默认采用该实现，将令牌信息保存在内存中，易于调试
     * JdbcTokenStore：令牌会被保存近关系型数据库，可以在不同服务器之间共享令牌
     * JwtTokenStore：使用 JWT 方式保存令牌，它不需要进行存储，但是它撤销一个已经授权令牌会非常困难，
     * 所以通常用来处理一个生命周期较短的令牌以及撤销刷新令牌,写法如下
     * return TokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
     * @return
     */
//    @Bean
//    public TokenStore tokenStore() {
//        //重写redis存储token配置（原生：return new RedisTokenStore(redisConnectionFactory);）
//        return new CustomRedisTokenStore(redisConnectionFactory);
//    }

}
