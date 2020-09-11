package com.qidian.mall.user.service;

import java.util.concurrent.CountDownLatch;

/**
 * 异步任务接口
 * @author bin
 */
public interface IAsyncService {


    /**
     * 测试多线程异步任务 效率对比
     * @param i
     * @throws InterruptedException
     */
    void task1(int i, CountDownLatch countDownLatch) throws InterruptedException;

    /**
     * 同步方法
     * @param i
     * @throws InterruptedException
     */
    void task2(int i) throws InterruptedException;
}
