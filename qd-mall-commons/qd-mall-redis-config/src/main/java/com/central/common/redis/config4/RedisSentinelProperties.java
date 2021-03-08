package com.central.common.redis.config4;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 哨兵配置
 */
@NoArgsConstructor
@Data
@ToString
public class RedisSentinelProperties {

    /**
     * 哨兵master 名称
     */
    private String master;

    /**
     * 哨兵节点
     */
    private String nodes;

    /**
     * 哨兵配置
     */
    private boolean masterOnlyWrite;

    /**
     * 失败重试次数
     */
    private int failMax;
}
