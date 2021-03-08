package com.central.common.redis.config4;


import com.central.common.redis.enums.RedisModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * redisson 客户端配置
 * @author sunbin
 * @date 2020/12/9
 * @description
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${spring.redis.mode}'=='single' or '${spring.redis.mode}'=='cluster' or '${spring.redis.mode}'=='sentinel'")
    protected class RedissonClientConfiguration {

        /**
         * 单机模式 redisson 客户端
         */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
        RedissonClient redissonSingle() {
            log.info(logInfo(RedisModeEnum.SINGLE.getCode(),redisProperties.getHost()+":"+redisProperties.getPort()));
            Config config = new Config();
            String node = redisProperties.getHost()+":"+redisProperties.getPort();
            node = node.startsWith("redis://") ? node : "redis://" + node;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(redisProperties.getPool().getConnTimeout())
                    .setConnectionPoolSize(redisProperties.getPool().getSize())
                    .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }


        /**
         * 集群模式的 redisson 客户端
         *
         * @return
         */
        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
        RedissonClient redissonCluster() {
            log.info(logInfo(RedisModeEnum.CLUSTER.getCode(),redisProperties.getCluster()));

            Config config = new Config();
            String[] nodes = redisProperties.getCluster().getNodes().split(",");
            List<String> newNodes = new ArrayList<>(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setScanInterval(
                            redisProperties.getCluster().getScanInterval())
                    .setIdleConnectionTimeout(
                            redisProperties.getPool().getSoTimeout())
                    .setConnectTimeout(
                            redisProperties.getPool().getConnTimeout())
                    .setRetryAttempts(
                            redisProperties.getCluster().getRetryAttempts())
                    .setRetryInterval(
                            redisProperties.getCluster().getRetryInterval())
                    .setMasterConnectionPoolSize(redisProperties.getCluster()
                            .getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redisProperties.getCluster()
                            .getSlaveConnectionPoolSize())
                    .setTimeout(redisProperties.getTimeout());
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * 哨兵模式 redisson 客户端
         * @return
         */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
        RedissonClient redissonSentinel() {
            log.info(logInfo(RedisModeEnum.SENTINEL.getCode(),redisProperties.getSentinel()));
            Config config = new Config();
            String[] nodes = redisProperties.getSentinel().getNodes().split(",");
            List<String> newNodes = new ArrayList<>(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(newNodes.toArray(new String[0]))
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .setReadMode(ReadMode.SLAVE)
                    .setRetryAttempts(redisProperties.getSentinel().getFailMax())
                    .setTimeout(redisProperties.getTimeout())
                    .setMasterConnectionPoolSize(redisProperties.getPool().getSize())
                    .setSlaveConnectionPoolSize(redisProperties.getPool().getSize());

            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }

            return Redisson.create(config);
        }


    }

    private String logInfo(String mode,Object properties){
        String desc = RedisModeEnum.getDescByCode(mode);
        return  "\n******************************************************"+
                "\n****** redisson       集成模式: " + desc +
                "\n****** redissonClient 配置详情：" +properties.toString()+
                "\n******************************************************";
    }

}
