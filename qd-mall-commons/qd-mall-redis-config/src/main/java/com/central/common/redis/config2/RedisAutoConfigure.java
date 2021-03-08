package com.central.common.redis.config2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis 配置类
 *
 * @author mall
 * @date 2018/11/6 11:02
 */
//@ConditionalOnClass(RedisRepository.class)
//@EnableConfigurationProperties({RedisProperties.class, CacheManagerProperties.class})
//@EnableCaching
public class RedisAutoConfigure {
//    @Autowired
//    private CacheManagerProperties cacheManagerProperties;

    /**
     * RedisTemplate配置
     * @param factory
     */
//    @Bean("redisTemplate2")
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//

//        // 为了开发方便，我们覆盖底层的object泛型为string
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(factory);
//        // jackson序列化配置、转义
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        // string序列化
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // hash的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // value序列化方式采用jackson
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // hash的value序列化方式采用jackson
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;

//    }

    /**
     * Redis repository redis repository.
     *
     * @param  the redis template
     * @return the redis repository
     */
//    @Bean
//    @ConditionalOnMissingBean
//    public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate2) {
//        return new RedisRepository(redisTemplate2);
//    }

//    @Bean(name = "cacheManager")
//    @Primary
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));
//
//        //自定义的缓存过期时间配置
//        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
//        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
//        if (configSize > 0) {
//            cacheManagerProperties.getConfigs().forEach(e -> {
//                RedisCacheConfiguration conf = getDefConf().entryTtl(Duration.ofSeconds(e.getSecond()));
//                redisCacheConfigurationMap.put(e.getKey(), conf);
//            });
//        }
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(difConf)
//                .withInitialCacheConfigurations(redisCacheConfigurationMap)
//                .build();
//    }
//
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return (target, method, objects) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getName());
//            sb.append(":" + method.getName() + ":");
//            for (Object obj : objects) {
//                sb.append(obj.toString());
//            }
//            return sb.toString();
//        };
//    }
//
//    private RedisCacheConfiguration getDefConf() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues()
//                .computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisObjectSerializer()));
//    }
}
