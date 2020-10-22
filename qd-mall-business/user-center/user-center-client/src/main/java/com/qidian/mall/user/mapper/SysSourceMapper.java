package com.qidian.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysSource;

public interface SysSourceMapper extends BaseMapper<SysSource> {

    /**
     * 逻辑删除 自动填充其他字段 其他字段需要添加注解  @TableField(value = "update_user",fill = FieldFill.UPDATE)
     * @param entity
     * @return
     */
    int deleteByIdWithFill(SysSource entity);
}