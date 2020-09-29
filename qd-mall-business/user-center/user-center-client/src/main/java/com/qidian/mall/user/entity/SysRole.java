package com.qidian.mall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.central.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_role")
public class SysRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2857086023681753580L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 角色code
     */
    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 角色名
     */
    @TableField(value = "role_name")
    private String roleName;



    public static final String COL_ID = "id";

    public static final String COL_ROLE_CODE = "role_code";

    public static final String COL_ROLE_NAME = "role_name";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DELETE_FLAG = "delete_flag";

    public static final String COL_VERSION = "version";
}