package com.qidian.mall.user.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色管理展示出参
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class UserRoleVo implements Serializable {
    private static final long serialVersionUID = -6938877370059200874L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 角色名
     */
    private String roleName;


}
