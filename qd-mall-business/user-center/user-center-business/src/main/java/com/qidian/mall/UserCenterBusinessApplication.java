package com.qidian.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableHystrix
@EnableFeignClients
//扫描到公共配置依赖里的相关配置（统一异常处理、国际化等）
@ComponentScan({"com.qidian.mall","com.central"})
//服务注册发现
@EnableDiscoveryClient
@SpringBootApplication
// mapper 扫描注解不要忘记，否则mapper注入失败
@MapperScan(basePackages = "com.qidian.mall.mapper")
public class UserCenterBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBusinessApplication.class, args);
    }

}