package com.qidian.mall.user.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;


import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 用户注册 入参
 * @Author binsun
 * @Date 2020-09-21
 * @Description
 */
@Data
public class RegUserDTO implements Serializable {
    private static final long serialVersionUID = -7190169437630177118L;

    public interface Add{}
    /**
     * 注册用户账号
     */
    @NotBlank(message = "注册用户账号不能为空",groups = {Add.class})
    private String username;
    /**
     * 注册用户密码
     */
    @NotBlank(message = "注册用户密码不能为空",groups = {Add.class})
    private String password;
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空",groups = {Add.class})
    private String confirmPassword;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号格式错误",groups = Add.class)
    @NotBlank(message = "注册用户账号不能为空",groups = {Add.class})
    private String mobile;
    /**
     * 短信验证
     */
    @NotBlank(message = "短信验证码不能为空",groups = {Add.class})
    private String smsCode;

    @NotBlank(message = "短信验证码id不能为空",groups = {Add.class})
    private String smsCodeId;


    /**
     * ============== 验证码校验 ================
     */
    /**
     * 1 android 2 ios 3 web
     */
    @NotNull(message = "平台类型不能为空", groups = {Add.class})
    @Range(min=1, max=3, message = "平台类型只能是[1,2,3]", groups = {Add.class})
    private Integer platformType;

    /**
     * 1:注册2:登录3:忘记密码
     */
    @NotNull(message = "业务类型不能为空", groups = {Add.class})
    @Range(min=1, max=3, message = "业务类型只能是[1:注册,2:登录,3:重置密码]", groups = {Add.class,})
    private Integer businessType;
    
    /**
     * 终端类型：1：手机 2：邮箱
     */
    @NotNull(message = "接收终端类型不能为空", groups = {Add.class})
    @Range(min=1, max=2, message = "接收终端类型只能是[1:手机,2：邮箱]", groups = {Add.class})
    private Integer receiveTerminalType;

    private String clientIp;
}
