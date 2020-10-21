package com.qidian.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 批量新增用户角色关系数据
     * @param sysUserRoleList
     * @return
     */
    Integer batchInsert(List<SysUserRole> sysUserRoleList);
}