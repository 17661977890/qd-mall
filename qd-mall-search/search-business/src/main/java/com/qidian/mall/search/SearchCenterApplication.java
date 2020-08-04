package com.qidian.mall.search;

import com.central.feign.common.interceptor.FeignInterceptorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(FeignInterceptorConfig.class)
//扫描到公共配置依赖里的相关配置（统一异常处理、国际化等）
@ComponentScan({"com.qidian.mall","com.central"})
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class SearchCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchCenterApplication.class, args);
    }
}
