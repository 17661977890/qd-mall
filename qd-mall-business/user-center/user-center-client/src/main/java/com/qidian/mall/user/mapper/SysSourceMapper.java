package com.qidian.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.response.RoleSourceVo;

import java.util.List;

public interface SysSourceMapper extends BaseMapper<SysSource> {

    /**
     * 逻辑删除 自动填充其他字段 其他字段需要添加注解  @TableField(value = "update_user",fill = FieldFill.UPDATE)
     * @param entity
     * @return
     */
    int deleteByIdWithFill(SysSource entity);

    /**
     * 查角色拥有得资源列表
     * @param roleId
     * @return
     */
    List<SysSource> getSourceListByRoleId(Long roleId);

    /**
     * 查看多个角色的资源列表
     * @param roleIds
     * @return
     */
    List<SysSource> getSourceListByRoleIds(List<Long> roleIds);

    /**
     * 登录用户获取 授权资源
     * @param roleIds
     * @return
     */
    List<RoleSourceVo> getSourceVoListByRoleIds(List<Long> roleIds);

    /**
     * 查用户拥有得资源列表（一用户 多角色 资源需去重）
     * @param userId
     * @return
     */
    List<SysSource> getSourceListByUserId(Long userId);
}