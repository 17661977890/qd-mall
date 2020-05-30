package com.central.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

/**
 * 基于twitter 雪花算法 ️的id生成器
 * @author bin
 */
public class IdWorker {

    private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);
    //起始时间戳
    private final static long START_STMP = 1480166465631L;

    //各部分占用的位数
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;  //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    //各部分的最大值
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    //各部分向左的位移位数
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

//    @Value("${IdWorker.datacenterId}")
    private long datacenterId;  //数据中心
//    @Value("${IdWorker.machineId}")
    private long machineId;    //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳
    private final Random RANDOM = new Random();//随机数生成下一毫秒第一个seq

    public IdWorker () {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            logger.warn("数据中心ID不能大于最大数据中心ID或小于0");
            throw new IllegalArgumentException("数据中心ID不能大于最大数据中心ID或小于0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            logger.warn("机器ID不能大于最大机器ID或小于0");
            throw new IllegalArgumentException("机器ID不能大于最大机器ID或小于0");
        }
    }

    public IdWorker (long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            logger.warn("数据中心ID不能大于最大数据中心ID或小于0");
            throw new IllegalArgumentException("数据中心ID不能大于最大数据中心ID或小于0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            logger.warn("机器ID不能大于最大机器ID或小于0");
            throw new IllegalArgumentException("机器ID不能大于最大机器ID或小于0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 生成下一个唯一ID
     * @return
     */
    public long nextId(){
        return nextId(getNewstmp());
    }

    /**
     * 生成指定时间的下一个Id
     * @param currStmp
     * @return
     */
    private synchronized long nextId(long currStmp){
        if(currStmp < lastStmp){
            //发生时钟回退
            long offset = lastStmp - currStmp;
            if(offset <= 5){
                //回退时间小于5，则等待
                try{
                    wait(offset << 1);
                }catch (InterruptedException e){
                    logger.error("发生时钟回退，线程等待，中断异常");
                    e.printStackTrace();
                }
            }else {
                logger.warn("发生时钟回退：且回退时间较长，放弃等待，重新获取时间戳！");
                return nextId();
            }
//            logger.warn("发生时钟回退：当前时间" + currStmp + "上一时间" + lastStmp);
//            currStmp = lastStmp;
        }

        if(currStmp == lastStmp){
            //相同毫秒内，序列号自增
            sequence = ++sequence & MAX_SEQUENCE;
            if (sequence == 0) {
                //同一毫秒生成的序列数已满
                //对下一毫秒第一个序列号取0-10的随机数
                sequence = RANDOM.nextInt(10);
                logger.warn(currStmp + "时间同一毫秒生成的序列数已满，重新获取时间戳！");
                return nextId();
            }
        }else{
            //新的毫秒
            //对下一毫秒第一个序列号取0-10的随机数
            sequence = RANDOM.nextInt(10);
        }
        //更新上一时间
        lastStmp = currStmp;

        return(currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT      //数据中心部分
                | machineId << MACHINE_LEFT            //机器标识部分
                | sequence;                            //序列号部分
    }

    private long getNewstmp(){
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        IdWorker idWorker = new IdWorker(2, 3);
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            System.out.println(idWorker.nextId());
        }
        System.out.println("生成10000个全局唯一Id耗时" + (System.nanoTime() - startTime) / 1000000 + "ms");

    }
}