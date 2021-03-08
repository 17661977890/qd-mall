package com.central.common.redis.lock2;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * redisson 分布式锁接口
 * @author sunbin
 * @date 2020-12-9
 * @description 参考：https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
 */
public interface DistributedLocker {

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 加锁 并设置自动释放锁时间
     * @param lockKey
     * @param timeout 释放时间，默认单位 秒
     * @return
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 加锁 并设置自动释放锁时间和具体时间单位
     * @param lockKey
     * @param unit
     * @param timeout
     * @return
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁的时间
     * @return
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 释放锁
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 释放锁
     * @param lock
     */
    void unlock(RLock lock);
}