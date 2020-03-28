package com.qidian.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.service.IAsyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * 异步任务web
 * @author bin
 */
@Slf4j
@Api(tags = {"异步任务web"})
@RequestMapping(value = "/async-controller")
@RestController
public class AsyncController extends BaseController {

    @Autowired
    private IAsyncService iAsyncService;


    /**
     * 线程池结合程序计数器：countDownLatch
     *
     * 可以换task2 查看异步 同步的不同之处
     * 对于业务逻辑需要用到的时候 就把 task1的方法逻辑换一下即可。
     * 常用场景： 大文件下载，导入导出，pdf文件上传转换ocr识别等等大数据量处理逻辑且与其他业务不相互影响
     * @return
     * @throws InterruptedException
     */
    @ApiOperation(value = "测试异步任务", notes = "测试异步任务")
    @RequestMapping(value = "/testJobMethod", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse testJobMethod() throws InterruptedException {
        log.info("===========测试异步方法的执行==========");
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(500);
        for (int i = 0; i < 500; i++) {
            iAsyncService.task1(i,countDownLatch);
        }
        countDownLatch.await();
        System.out.println((System.currentTimeMillis() - start));
        log.info("===========测试异步方法的执行结束===========");
        return RestResponse.resultSuccess(null);
    }
}
