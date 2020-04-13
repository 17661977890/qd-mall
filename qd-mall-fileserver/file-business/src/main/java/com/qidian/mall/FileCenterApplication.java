package com.qidian.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 文件存储服务--暂用阿里的oss
 * @Author binsun
 * @Date 2020-01-16
 * @Description
 */
//扫描到公共配置依赖里的相关配置（统一异常处理、国际化等）
@ComponentScan({"com.qidian.mall","com.central"})
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class FileCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileCenterApplication.class, args);
    }

}
