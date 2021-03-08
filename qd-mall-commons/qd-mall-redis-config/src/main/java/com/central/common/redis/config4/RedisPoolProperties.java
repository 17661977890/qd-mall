package com.central.common.redis.config4;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Abbot
 * @des redis 池配置
 * @date 2018/10/18 10:43
 **/
@NoArgsConstructor
@Data
@ToString
public class RedisPoolProperties {


    private int maxIdle;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int connTimeout;

    private int soTimeout;

    /**
     * 池大小
     */
    private  int size;

}
