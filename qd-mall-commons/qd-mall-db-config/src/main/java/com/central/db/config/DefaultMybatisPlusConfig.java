package com.central.db.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * mybatis-plus配置
 * @author mall
 * @date 2018/12/13
 */
@Import(DateMetaObjectHandler.class)
@Configuration
public class DefaultMybatisPlusConfig {
    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MySqlInjector myLogicSqlInjector() {
        return new MySqlInjector();
    }



}
