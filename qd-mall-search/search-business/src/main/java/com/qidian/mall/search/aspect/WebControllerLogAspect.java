package com.qidian.mall.search.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(5)
@Component
public class WebControllerLogAspect extends BaseControllerLogAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * com.qidian.mall.search.controller..*.*(..))")
    public void controllerLog(){}

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        logSystemBefore(logger, joinPoint);
    }

    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        try {
            // 处理完请求，返回内容
            logSystemAfter(logger, ret, startTime.get());
        }finally {
            startTime.remove();
        }
    }
}

