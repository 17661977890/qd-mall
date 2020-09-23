package com.central.auth.common.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置需要放权的url白名单
 *
 * @author bin
 */
@Setter
@Getter
public class PermitProperties {
    /**
     * 应用监控、数据源监控中心druid 和swagger需要访问的url 等公共服务请求配置不需要认证即可访问
     */
    private static final String[] ENDPOINTS = {
            "/actuator/**",
            "/*/v2/api-docs",
            "/v2/api-docs",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**",
            "/csrf",
            "/"
    };

    /**
     * 设置不用认证的url ---在配置文件中配置获取
     */
    private String[] httpUrls = {};

    /**
     * 设置认证后不需要判断具体权限的url，所有登录的人都能访问
     */
    private String[] menusPaths = {};

    public String[] getUrls() {
        if (httpUrls == null || httpUrls.length == 0) {
            return ENDPOINTS;
        }
        List<String> list = new ArrayList<>();
        for (String url : ENDPOINTS) {
            list.add(url);
        }
        for (String url : httpUrls) {
            list.add(url);
        }
        return list.toArray(new String[list.size()]);
    }
}
