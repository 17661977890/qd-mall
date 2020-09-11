package com.qidian.mall.uaa.oauthconfig;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.TimeUnit;

/**
 * 授权码管理：
 * 底层只有内存模式和jdbc模式，如果想实现redis模式，需要自己去实现接口或者继承 RandomValueAuthorizationCodeServices 类
 * JdbcAuthorizationCodeServices替换
 * 如果不自定授权码长度，
 * @author sunbin
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 替换JdbcAuthorizationCodeServices的存储策略
     * 将存储code到redis，并设置过期时间，10分钟
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redisTemplate.opsForValue().set(redisKey(code), authentication, 10, TimeUnit.MINUTES);
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        String codeKey = redisKey(code);
        OAuth2Authentication token = (OAuth2Authentication) redisTemplate.opsForValue().get(codeKey);
        this.redisTemplate.delete(codeKey);
        return token;
    }

    /**
     *自定义授权码长度（底层code 授权码长度默认6）
     */
    private RandomValueStringGenerator generator = new RandomValueStringGenerator(12);

    /**
     * 如果自定义授权码长度需要重写此方法
     * @param authentication
     * @return
     */
    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        this.store(code, authentication);
        return code;
    }

    /**
     * redis中 code key的前缀
     *
     * @param code
     * @return
     */
    private String redisKey(String code) {
        return "oauth:code:" + code;
    }
}
