package com.qidian.mall.tokenstore.config;

import com.qidian.mall.tokenstore.db.AuthDbTokenStore;
import com.qidian.mall.tokenstore.jwt.AuthJwtTokenStore;
import com.qidian.mall.tokenstore.jwt.ResJwtTokenStore;
import com.qidian.mall.tokenstore.redis.AuthRedisTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * token存储配置
 * Spring Boot通过@ConditionalOnProperty来控制Configuration是否生效
 * 如果类型type 在yml配置文件中有值，与havingValue表 一样，则返回true，则 @Configuration 配置生效，可以注入bean使用
 * 否则返回false ，配置无效
 * @author mall
 */
public class TokenStoreConfig {
    @Configuration
    @ConditionalOnProperty(prefix = "mall.oauth2.token.store", name = "type", havingValue = "db")
    @Import(AuthDbTokenStore.class)
    public class JdbcTokenConfig {
    }

    @Configuration
    @ConditionalOnProperty(prefix = "mall.oauth2.token.store", name = "type", havingValue = "redis", matchIfMissing = true)
    @Import(AuthRedisTokenStore.class)
    public class RedisTokenConfig {
    }

    @Configuration
    @ConditionalOnProperty(prefix = "mall.oauth2.token.store", name = "type", havingValue = "authJwt")
    @Import(AuthJwtTokenStore.class)
    public class AuthJwtTokenConfig {
    }

    @Configuration
    @ConditionalOnProperty(prefix = "mall.oauth2.token.store", name = "type", havingValue = "resJwt")
    @Import(ResJwtTokenStore.class)
    public class ResJwtTokenConfig {
    }
}
