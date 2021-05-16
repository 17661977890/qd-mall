package com.qidian.mall.user.service;

import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.request.UserRoleDTO;

import java.util.List;

/**
 * 用户角色关系业务类
 * @Author binsun
 * @Date 2020-10-21
 * @Description
 */
public interface ISysUserRoleService {

    /**
     *  批量保存用户角色关系数据
     * @param userRoleDTO
     * @return
     */
    Boolean saveUserRole(UserRoleDTO userRoleDTO,String username);

    /**
     * 获取用户的角色列表信息
     * @return
     */
    List<SysRole> getRoleByUserId(Long userId);

    /**
     * 获取角色下用户列表信息
     * @param roleId
     * @return
     */
    List<SysUser> getUserByRoleId(Long roleId);
}
