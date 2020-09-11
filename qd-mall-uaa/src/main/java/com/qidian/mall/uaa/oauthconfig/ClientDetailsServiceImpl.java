package com.qidian.mall.uaa.oauthconfig;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * 加载客户端信息，添加了缓存配置（将oauth_client_details表数据缓存到redis，这里做个缓存优化）
 *
 * 客户端重要的属性是：
 * clientId: （必须的）客户端 id
 * secret: （要求用于受信任的客户端）客户端的机密，如果有的话
 * scope: 客户范围限制。如果范围未定义或为空（默认），客户端将不受范围限制
 * authorizedGrantTypes: 授权客户端使用的授予类型。默认值为空
 * authorities: 授权给客户的认证（常规 Spring Security 认证）
 *
 * 通过直接访问底层存储（例如 JdbcClientDetailsService 用例中的数据库表）
 * 或者通过 ClientDetailsManager 接口（ClientDetailsService 也能实现这两种实现），
 * 可以在正在运行的应用程序中更新客户端详细信息。
 **/
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Resource
    DataSource dataSource;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private final String CACHE_KEY = "CACHE_CLIENT_DETAILS";

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return getClientDetails(clientId);
    }

    /**
     * （1）先去redis缓存取client信息，没有在通过底层业务类访问底层数据库：JdbcClientDetailsService
     * @param clientId
     * @return
     */
    private ClientDetails getClientDetails(String clientId) {
        if (redisTemplate.hasKey(CACHE_KEY)) {
            ClientDetails clientDetails = (ClientDetails) redisTemplate.opsForValue().get(CACHE_KEY+":"+clientId);
            if (clientDetails !=null) {
                return clientDetails;
            }
        }
        return loadClientDetails(clientId);
    }

    /**
     * （2）通过访问底层存储，来更新客户端配置信息（并缓存到redis中）
     * 通过访问数据库加载客户端配置 oauth_client_details表 中预先配置了数据
     * @param clientId
     * @return
     */
    private ClientDetails loadClientDetails(String clientId) {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
        if(clientDetails == null) {
            throw new ClientRegistrationException("应用" + clientId + "不存在!");
        }
        cacheClientDetails(clientDetails);
        return clientDetails;
    }

    private void cacheClientDetails(ClientDetails clientDetails) {
        redisTemplate.opsForValue().set(CACHE_KEY+":"+clientDetails.getClientId(), clientDetails);
    }

}
