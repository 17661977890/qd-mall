package com.central.common.redis.config4;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis 通用配置文件
 * @author sunbin
 * @date 2021-1-12
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = false)
public class RedisProperties {

    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;
    /**
     * 密码
     */
    private String password;
    /**
     * redis 节点模式
     */
    private String mode;

    /**
     * 单节点下的 ip
     */
    private String host;
    /**
     * 单节点下的 端口
     */
    private String port;
    /**
     * 自动释放锁的时间：单位 秒 s
     */
    private int autoReleaseLockTime;

    /**
     * 池配置
     */
    private RedisPoolProperties pool;

    /**
     * 集群 信息配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;

}