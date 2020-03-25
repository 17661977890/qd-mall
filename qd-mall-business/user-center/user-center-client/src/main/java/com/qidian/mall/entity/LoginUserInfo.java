package com.qidian.mall.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户信息
 */
@Data
public class LoginUserInfo implements Serializable {

    private String username;

    private String headPhoto;
}
