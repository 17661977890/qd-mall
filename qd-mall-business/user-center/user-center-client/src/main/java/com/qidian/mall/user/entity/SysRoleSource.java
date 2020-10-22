package com.qidian.mall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色资源关系实体
 */
@Data
@TableName(value = "sys_role_source")
public class SysRoleSource {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 资源id
     */
    @TableField(value = "source_id")
    private Long sourceId;

    public static final String COL_ID = "id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_SOURCE_ID = "source_id";
}