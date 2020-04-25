package com.qidian.mall.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户信息
 */
@Data
public class LoginUserInfo implements Serializable {


    private String username;

    private String headPhoto;

    private String nickname;


    private String headImgUrl;

    private String mobile;

    private Boolean sex;
}
