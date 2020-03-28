package com.central.base.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务 线程池配置
 */
@Configuration
public class SchedulingConfig {
    @Bean
    public TaskScheduler taskScheduler(){
        /*
         * ThreadPoolTaskScheduler：线程池任务调度类，能够开启线程池进行任务调度。
         * ThreadPoolTaskScheduler.schedule()方法会创建一个定时计划ScheduledFuture，在这个方法需要添加两个参数，Runnable（线程接口类） 和CronTrigger（定时任务触发器）
         */
        ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();

        // 定时任务执行线程池核心线程数
        poolTaskScheduler.setPoolSize(20);
        poolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskScheduler.setAwaitTerminationSeconds(120);
        // 开启 remove-on-cancel
        poolTaskScheduler.setRemoveOnCancelPolicy(true);
        poolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler--");
        return poolTaskScheduler;
    }
}