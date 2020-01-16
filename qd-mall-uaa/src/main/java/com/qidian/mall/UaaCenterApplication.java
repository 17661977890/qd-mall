package com.qidian.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 基于oauth2的鉴权中心
 * @Author binsun
 * @Date 2020-01-16
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UaaCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaCenterApplication.class, args);
    }

}
