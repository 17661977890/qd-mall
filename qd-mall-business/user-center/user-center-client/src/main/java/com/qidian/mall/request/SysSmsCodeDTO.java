package com.qidian.mall.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
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


    /**
     * 客户端ip
     */
    private String clientIp;
    /**
     * 1 android 2 ios 3 web
     */
    @NotBlank(message = "平台类型不能为空", groups = {Send.class})
    @Pattern(regexp = "[1-3]{1}", message = "平台类型只能是[1,2,3]", groups = {Send.class})
    private String platformType;

    /**
     * 1:注册2:登录3:忘记密码
     */
    @NotBlank(message = "业务类型不能为空", groups = {Send.class})
    @Pattern(regexp = "[1-3]{1}", message = "业务类型只能是[1,2,3]", groups = {Send.class,})
    private String businessType;

    /**
     * 有可能是邮箱或者手机 ---正则匹配在业务层或者controller做
     */
    @NotBlank(message = "接收终端号码不能为空", groups = {Send.class,})
    private String receiveTerminalNo;

    /**
     * 终端类型：1：手机 2：邮箱
     */
    @NotBlank(message = "接收终端类型不能为空", groups = {Send.class})
    @Pattern(regexp = "[1-2]{1}", message = "接收终端类型只能是[1:手机,2：邮箱]", groups = {Send.class})
    private String receiveTerminalType;
}
