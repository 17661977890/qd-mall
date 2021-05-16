package com.qidian.mall.user.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 角色资源入参
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class RoleSourceDTO implements Serializable {
    private static final long serialVersionUID = 7497704689865762041L;

    public interface  Add{}

    public interface RoleSource{}

    public interface UserSource{}

    public interface RoleListSource{}

    @NotNull(message = "角色id不能为空",groups = {RoleSource.class,Add.class})
    private Long roleId;

    @NotEmpty(message = "角色集合不能为空",groups = {RoleListSource.class})
    private List<Long> roleIds;

    @NotNull(message = "资源id集合不能为空",groups = {Add.class})
    private List<Long> sourceIdList;

    // 查用户资源传参
    @NotNull(message = "用户id不能为空",groups = UserSource.class)
    private Long userId;
}
