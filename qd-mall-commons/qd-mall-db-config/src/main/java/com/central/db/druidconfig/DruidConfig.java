package com.central.db.druidconfig;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Druid配置类
 * @Author: bin
 */
//@Configuration
public class DruidConfig {

    private static final Logger logger = LoggerFactory.getLogger(DruidConfig.class);



    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        logger.info("--------------init Druid Servlet DruidConfig--------------");
        ServletRegistrationBean<StatViewServlet> srBean = new ServletRegistrationBean<>();
        srBean.setServlet(new StatViewServlet());
        srBean.addUrlMappings("/druid/*");
        // IP白名单
        srBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
//        srBean.addInitParameter("deny", "127.0.0.1");
        //控制台管理用户
        srBean.addInitParameter("loginUsername", "admin");
        srBean.addInitParameter("loginPassword", "admin");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        srBean.addInitParameter("resetEnable", "false");
        return srBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterBean = new FilterRegistrationBean<>(new WebStatFilter());
        filterBean.addUrlPatterns("/*");
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterBean.setInitParameters(initParams);
        return filterBean;
    }
}
