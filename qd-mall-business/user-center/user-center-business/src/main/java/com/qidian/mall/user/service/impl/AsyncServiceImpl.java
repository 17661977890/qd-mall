package com.qidian.mall.user.service.impl;

import com.qidian.mall.user.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * 异步任务实现类
 * @author bin
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Async
    public void task1(int i, CountDownLatch countDownLatch) throws InterruptedException {
        Thread.sleep(10000);
        log.info("异步执行：{}=={}=={}",Thread.currentThread().getId(),Thread.currentThread().getName(),i);
        countDownLatch.countDown();
    }

    public void task2(int i) throws InterruptedException {
        Thread.sleep(10);
        log.info("顺序执行：{}{}{}",Thread.currentThread().getId(),Thread.currentThread().getName(),i);
    }
}
