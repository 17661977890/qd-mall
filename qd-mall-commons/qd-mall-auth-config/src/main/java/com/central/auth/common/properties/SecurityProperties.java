package com.central.auth.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

/**
 * 相关认证鉴权的过滤开关配置
 * @author bin
 * @date 2020-04-19
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "mall.security")
@RefreshScope
public class SecurityProperties {
    /**
     * 忽略认证的地址匹配规则
     * 1、各模块yml配置文件配置 自己模块的过滤请求
     * 2、属性类内部配置公共服务的相关请求（主要是一些公共服务，如swagger、druid监控、actuator监控等）
     */
    private PermitProperties ignore = new PermitProperties();
    /**
     * 权限拦截开关配置 --- 主要是在网关模块做用户角色权限统一拦截使用
     */
    private AuthProperties auth = new AuthProperties();

    /**
     * 客户端是否需要验证码的配置 ---主要是认证服务器中使用
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();


}
