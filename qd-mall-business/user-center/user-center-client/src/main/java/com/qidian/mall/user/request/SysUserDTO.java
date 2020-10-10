package com.qidian.mall.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 系统用户名DTO 入参对象
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="SysUserDTO入参对象", description="系统用户")
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface Add{}
    public interface Update{}
    public interface Id{}


    @ApiModelProperty(value = "主键id")
    @NotNull(message = "主键id不能为空",groups = {Update.class,Id.class})
    private Long id;

    @NotBlank(message = "用户名不能为空",groups = {Add.class,Update.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "昵称不能为空",groups = {Add.class,Update.class})
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @NotBlank(message = "手机号不能为空",groups = {Add.class,Update.class})
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @NotNull(message = "性别不能为空",groups = {Add.class,Update.class})
    @Range(min = 0,max = 1,message = "性别只能使用1：男，0：女 标识",groups = {Add.class,Update.class})
    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "用户注册来源ip")
    private String clientIp;

    @Email
    @NotBlank(message = "邮箱不能为空",groups = {Add.class,Update.class})
    @ApiModelProperty(value = "邮箱")
    private String email;


}
