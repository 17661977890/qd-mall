package com.qidian.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.entity.SysRoleSource;
import com.qidian.mall.user.entity.SysUserRole;

import java.util.List;

public interface SysRoleSourceMapper extends BaseMapper<SysRoleSource> {


    /**
     * 批量新增用户角色关系数据
     * @param sysRoleSourceList
     * @return
     */
    Integer batchInsert(List<SysRoleSource> sysRoleSourceList);
}