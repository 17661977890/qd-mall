package com.qidian.mall.user.response;

import com.baomidou.mybatisplus.annotation.*;
import com.central.base.mvc.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色管理展示出参
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class SysRoleVo implements Serializable {
    private static final long serialVersionUID = -6938877370059200874L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 删除标识（Y、N）
     */
    private String deleteFlag;

    /**
     * 数据版本
     */
    private String version;
}
