package com.qidian.mall.user.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色信息存储入参
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 933810288099662215L;

    public interface Add{}
    public interface Update{}
    public interface Id{}

    @NotNull(message = "角色id不能为空",groups = {Update.class,Id.class})
    private Long id;

    @NotBlank(message = "角色名称不能为空",groups = {Add.class,Update.class})
    private String roleName;

    @NotBlank(message = "角色编码不能为空",groups = {Add.class,Update.class})
    private String roleCode;


}
