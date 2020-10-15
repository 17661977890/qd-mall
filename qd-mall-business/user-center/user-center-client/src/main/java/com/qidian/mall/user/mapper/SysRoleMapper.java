package com.qidian.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.entity.SysRole;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 逻辑删除 自动填充其他字段 其他字段需要添加注解  @TableField(value = "update_user",fill = FieldFill.UPDATE)
     * @param entity
     * @return
     */
    int deleteByIdWithFill(SysRole entity);
}