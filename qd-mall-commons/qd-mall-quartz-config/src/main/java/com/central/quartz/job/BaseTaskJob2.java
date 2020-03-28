package com.central.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author bin
 * @Description: 基础任务调度taskJob接口
 * <p>
 */
public interface BaseTaskJob2 extends Job {
    void execute(JobExecutionContext context) throws JobExecutionException;
}