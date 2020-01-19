package com.central.base.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密器--引入的几个方式（java config方式创建注入bean）：
 * （1）在有 @Configuration 注解的类中 @Import(PasswordEncoderUtil.class) 引入密码加密器，此模块即可使用bean
 * （2）在 @Bean 的所属类或者子类中加 @Configuration 或者@Component 注解，就可以在含有此类的模块下注入
 *
 * @Author binsun
 * @Date 2020-01-19
 * @Description
 */
@Configuration
public class PasswordEncoderUtil {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
