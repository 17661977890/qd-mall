package com.qidian.mall.user.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author binsun
 * @Date 2020-09-10
 * @Description
 */
@Data
public class SysSmsCodeDTO implements Serializable {
    private static final long serialVersionUID = 2385557050178421729L;

    public interface Send{}

    public interface Verify{};


    /**
     * 客户端ip
     */
    private String clientIp;
    /**
     * 1 android 2 ios 3 web
     */
    @NotNull(message = "平台类型不能为空", groups = {Send.class,Verify.class})
    @Range(min=1, max=3, message = "平台类型只能是[1,2,3]", groups = {Send.class,Verify.class})
    private Integer platformType;

    /**
     * 1:注册2:登录3:忘记密码
     */
    @NotNull(message = "业务类型不能为空", groups = {Send.class,Verify.class})
    @Range(min=1, max=3, message = "业务类型只能是[1:注册,2:登录,3:重置密码]", groups = {Send.class,Verify.class})
    private Integer businessType;

    /**
     * 有可能是邮箱或者手机 ---正则匹配在业务层或者controller做
     */
    @NotBlank(message = "接收终端号码不能为空", groups = {Send.class,Verify.class})
    private String receiveTerminalNo;

    /**
     * 终端类型：1：手机 2：邮箱
     */
    @NotNull(message = "接收终端类型不能为空", groups = {Send.class})
    @Range(min=1, max=2, message = "接收终端类型只能是[1:手机,2：邮箱]", groups = {Send.class,Verify.class})
    private Integer receiveTerminalType;


    /**
     * ==================== 后台验证 =====================
     */
    @NotNull(message = "验证码不能为空", groups = {Verify.class})
    private String verificationCode;
    @NotNull(message = "短信验证码id不能为空", groups = {Verify.class})
    private String smsCodeId;
}
