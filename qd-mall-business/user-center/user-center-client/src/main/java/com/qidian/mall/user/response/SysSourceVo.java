package com.qidian.mall.user.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源出参实体
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class SysSourceVo implements Serializable {
    private static final long serialVersionUID = -6642228584166779407L;

    private Long id ;
    /** 资源名称 */
    private String sourceName ;
    /**
     * 资源code
     */
    private String sourceCode;
    /** 上级资源id */
    private Long parentId ;
    /** 上级资源名称 */
    private String parentName ;
    /** 资源类型 */
    private Integer sourceType ;
    /** 资源路径url */
    private String url ;
    /** 排序号 */
    private Integer sort ;
    /** 是否显示;显示：Y、隐藏：N */
    private String showFlag ;
    /** 是否删除 */
    private String deleteFlag ;
    /** 乐观锁 */
    private Integer version ;
    /** 创建人 */
    private String createUser ;
    /** 创建时间 */
    private Date createTime ;
    /** 更新人 */
    private String updateUser ;
    /** 更新时间 */
    private Date updateTime ;
}
