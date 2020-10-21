package com.qidian.mall.user.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户角色入参
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class UserRoleDTO implements Serializable {
    private static final long serialVersionUID = 3097239994247631659L;

    @NotNull(message = "用户id不能为空")
    private Long userId;

    private List<Long> roleIdList;
}
