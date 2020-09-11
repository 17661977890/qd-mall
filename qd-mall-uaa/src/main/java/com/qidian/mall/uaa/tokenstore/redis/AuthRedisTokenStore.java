package com.qidian.mall.uaa.tokenstore.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证服务器使用Redis存取令牌
 * 注意: 需要配置redis参数
 *
 * @author mall
 * @date 2018/7/25 9:36
 */
public class AuthRedisTokenStore {
    /**
     * 通过RedisConnectionFactory工厂创建RedisConnection
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        // 自定义实现redis token存储
        return new CustomRedisTokenStore(redisConnectionFactory);
    }
}
