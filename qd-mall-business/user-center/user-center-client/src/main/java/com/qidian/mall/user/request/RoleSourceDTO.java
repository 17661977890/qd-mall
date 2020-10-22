package com.qidian.mall.user.request;

import lombok.Data;

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

    @NotNull(message = "角色id不能为空")
    private Long roleId;


    private List<Long> sourceIdList;

    // 查用户资源传参
    private Long userId;
}
