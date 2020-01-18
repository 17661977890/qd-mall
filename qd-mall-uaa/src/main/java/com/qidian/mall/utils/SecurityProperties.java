package com.qidian.mall.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;

/**
 * @author mall
 * @date 2019/1/4
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "mall.security")
@RefreshScope
public class SecurityProperties {

    private PermitProperties ignore = new PermitProperties();
}
