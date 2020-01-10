package com.qidian.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
// mapper 扫描注解不要忘记，否则mapper注入失败
@MapperScan(basePackages = "com.qidian.mall.mapper")
public class UserCenterBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBusinessApplication.class, args);
    }

}