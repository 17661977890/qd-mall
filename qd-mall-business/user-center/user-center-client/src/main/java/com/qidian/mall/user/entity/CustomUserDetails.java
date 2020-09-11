package com.qidian.mall.user.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义用户认证实体
 * SocialUserDetails继承 UserDetails 后者是Spring Security中一个核心的接口。
 * 其中定义了一些可以获取用户名、密码、权限等与认证相关的信息的方法。
 * @Author binsun
 * @Date 2020-01-18
 * @Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomUserDetails extends SysUser implements SocialUserDetails {

    /**
     * 用户角色信息
     */
    private List<Integer> roles;
    /**
     * 客户端id
     */
    private String clientId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 默认是false 需要改为true，否则登录是报错：User account is locked
     *
     * 其余的属性也改为true
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 暂时写为true，可以使用实体的属性来赋值
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserId() {
        return getOpenId();
    }
}
