package com.qidian.mall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.central.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统资源表实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_source")
public class SysSource extends BaseEntity {
    /**
     * 资源id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 资源名称
     */
    @TableField(value = "source_name")
    private String sourceName;

    /**
     * 资源code
     */
    @TableField(value = "source_code")
    private String sourceCode;

    /**
     * 上级资源id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 上级资源名称
     */
    @TableField(value = "parent_name")
    private String parentName;

    /**
     * 资源类型 1:目录 2:菜单 3:按钮
     */
    @TableField(value = "source_type")
    private Integer sourceType;

    /**
     * 资源路径url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 排序号
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 是否显示 显示：Y、隐藏：N
     */
    @TableField(value = "show_flag")
    private String showFlag;

    /**
     *  是不是父亲节点 --------- 目录和菜单都是父节点  按钮 是最底层节点 所以不是
     *  Y N
     */
    @TableField(value = "is_parent")
    private String isParent;


    public static final String COL_ID = "id";

    public static final String COL_SOURCE_NAME = "source_name";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_PARENT_NAME = "parent_name";

    public static final String COL_SOURCE_TYPE = "source_type";

    public static final String COL_URL = "url";

    public static final String COL_SORT = "sort";

    public static final String COL_SHOW_FLAG = "show_flag";

    public static final String COL_DELETE_FLAG = "delete_flag";

    public static final String COL_VERSION = "version";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_PARENT = "is_parent";
}