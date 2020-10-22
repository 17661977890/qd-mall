package com.qidian.mall.user.service;

import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.request.RoleSourceDTO;
import com.qidian.mall.user.request.UserRoleDTO;

import java.util.List;

/**
 * 角色权限关系业务类
 * @Author binsun
 * @Date 2020-10-21
 * @Description
 */
public interface ISysRoleSourceService {

    /**
     *  批量保存角色权限关系数据
     * @param roleSourceDTO
     * @return
     */
    Boolean saveRoleSource(RoleSourceDTO roleSourceDTO, String username);

    /**
     * 获取角色得资源权限列表信息
     * @return
     */
    List<SysSource> getSourceListByRoleId(Long roleId);

    /**
     * 获取用户得资源权限列表信息 (用户--角色--资源 （角色共有资源去重）)
     * @return
     */
    List<SysSource> getSourceListByUserId(Long userId);
}
