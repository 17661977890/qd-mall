package com.central.common.redis.lock2;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置类
 */
//@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;


    //添加redisson的bean
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        //此示例是单机的，可以是主从、sentinel、集群等模式
        String node = host+":"+port;
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(10000)
                .setConnectionPoolSize(30)
                .setConnectionMinimumIdleSize(1);
        return (Redisson) Redisson.create(config);
    }
}