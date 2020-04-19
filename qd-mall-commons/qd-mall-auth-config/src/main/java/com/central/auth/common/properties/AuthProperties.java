package com.central.auth.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限拦截配置
 * @author mall
 */
@Setter
@Getter
public class AuthProperties {
    /**
     * 要认证的url
     */
    private String[] httpUrls = {};

    /**
     * 是否开启url权限验证（权限拦截开关）
     */
    private boolean urlEnabled = false;
}
