package com.central.feign.common.configuration;

import com.central.feign.common.interceptor.FeignRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 全局配置
 * <p>
 * Description:
 * </p>
 *
 * @author bin
 * @date 2020-04-21
 */
//@Configuration
public class FeignRequestConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

}