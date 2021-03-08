package com.central.common.redis.config4;

import com.central.common.redis.enums.RedisModeEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Redis 多元配置
 *
 * @author sunbin
 *
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class CommonRedisConfig {

    private static final String SEPARATOR=",";



    @Autowired
    private RedisProperties redisProperties;

    @Bean("redisTemplate3")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisSerializer<Object> serializer = redisSerializer2();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer2(){
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //必须设置，否则无法将JSON转化为对象，会转化成Map类型
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisCacheManager redisCacheManager2(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer2())).entryTtl(Duration.ofDays(1));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(LettuceClientConfiguration lettuceClientConfiguration ) {
        try {
            //解密redis密码 若配置文件使用的明文密码则不需要
            String mode = redisProperties.getMode();
            if (RedisModeEnum.SINGLE.getCode().equals(mode)) {
                //单机模式
                log.info(logInfo(mode));
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
                configuration.setHostName(redisProperties.getHost());
                configuration.setPort(Integer.parseInt(redisProperties.getPort()));
                configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
                configuration.setDatabase(redisProperties.getDatabase());
                return new LettuceConnectionFactory(configuration,lettuceClientConfiguration);
            } else if(RedisModeEnum.CLUSTER.getCode().equals(mode)){
                //集群模式
                log.info(logInfo(mode));
                String clusterNodes =redisProperties.getCluster().getNodes();
                String[] serverArray = clusterNodes.split(",");
                Set<RedisNode> nodes = new HashSet<RedisNode>();
                for (String ipPort : serverArray) {
                    String[] ipAndPort = ipPort.split(":");
                    nodes.add(new RedisNode(ipAndPort[0].trim(), Integer.parseInt(ipAndPort[1])));
                }
                RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
                redisClusterConfiguration.setClusterNodes(nodes);
                redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
                redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
                return new LettuceConnectionFactory(redisClusterConfiguration,lettuceClientConfiguration);
            }else if(RedisModeEnum.SENTINEL.getCode().equals(mode)){
                log.info(logInfo(mode));
                RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
                String[] host = redisProperties.getSentinel().getNodes().split(SEPARATOR);
                for(String redisHost : host){
                    String[] item = redisHost.split(":");
                    String ip = item[0];
                    String port = item[1];
                    configuration.addSentinel(new RedisNode(ip, Integer.parseInt(port)));
                }
                configuration.setMaster(redisProperties.getSentinel().getMaster());
                configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
                return new LettuceConnectionFactory(configuration,lettuceClientConfiguration);
            }
        } catch (Exception e) {
            log.error("redis连接失败 请检查密码配置", e);
        }
        return null;
    }


    /**
     * 配置LettuceClientConfiguration 包括线程池配置和安全项配置
     * @param genericObjectPoolConfig common-pool2线程池
     * @return lettuceClientConfiguration
     */
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(Duration.ofMillis(redisProperties.getTimeout()))
                .build();
    }

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        poolConfig.setMaxTotal(redisProperties.getPool().getSize());
        poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        //todo 其他配置
        return poolConfig;
    }


    private String logInfo(String mode){
        String desc = RedisModeEnum.getDescByCode(mode);
        return  "\n**********************************"+
                "\n*******   redis 配置模式: "  + desc +
                "\n**********************************";
    }

}
