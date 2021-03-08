package com.central.common.redis.lock2;

import com.central.common.redis.enums.RedisModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的具体实现（根据配置兼容不同部署环境）
 */
@Slf4j
@Component
public class RedissonDistributedLocker implements DistributedLocker {

    @Value("spring.redis.mode")
    private String mode;

    @Autowired
    private RedissonClient redissonSingle;

    @Autowired
    private RedissonClient redissonCluster;

    @Autowired
    private RedissonClient redissonSentinel;

    @Override
    public RLock lock(String lockKey) {
        RedissonClient redissonClient = init();
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RedissonClient redissonClient = init();
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }
    
    @Override
    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        RedissonClient redissonClient = init();
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }
    
    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RedissonClient redissonClient = init();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    @Override
    public void unlock(String lockKey) {
        RedissonClient redissonClient = init();
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
    
    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

   private RedissonClient init(){
        if(RedisModeEnum.CLUSTER.getCode().equals(mode)){
            log.info("集群模式的redissonClient");
            return redissonCluster;
        }
        if(RedisModeEnum.SENTINEL.getCode().equals(mode)){
            log.info("哨兵模式的redissonClient");
            return redissonSentinel;
        }
        log.info("单机模式的redissonClient");
        return redissonSingle;
   }
}