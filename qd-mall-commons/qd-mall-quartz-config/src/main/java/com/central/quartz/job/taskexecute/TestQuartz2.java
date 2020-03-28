package com.central.quartz.job.taskexecute;

import com.central.quartz.job.BaseTaskJob2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义任务执行类
 * @author bin
 */
@Component
public class TestQuartz2 implements BaseTaskJob2 {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public  int i = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        i++;
        logger.error("task2>>>>>>>  " + i);

        try {

//            QuartzJobManager.getInstance().pauseJob("testJob","Group1");
//            QuartzJobManager.getInstance().jobdelete(this.getClass().getSimpleName(),"ah");//执行完此任务就删除自己
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}