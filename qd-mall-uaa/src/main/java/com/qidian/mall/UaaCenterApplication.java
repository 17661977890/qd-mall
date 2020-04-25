package com.qidian.mall;

import com.central.base.util.PasswordEncoderUtil;
import com.central.feign.common.interceptor.FeignInterceptorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 基于oauth2的鉴权中心
 * @Author binsun
 * @Date 2020-01-16
 * @Description
 */
@Import(FeignInterceptorConfig.class)
//扫描到公共配置依赖里的相关配置（统一异常处理、国际化等）
@ComponentScan({"com.qidian.mall","com.central"})
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class UaaCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaCenterApplication.class, args);
    }

}
