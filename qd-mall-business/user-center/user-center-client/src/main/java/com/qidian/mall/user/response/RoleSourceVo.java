package com.qidian.mall.user.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色授权资源出参实体
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class RoleSourceVo implements Serializable {
    private static final long serialVersionUID = -6642228584166779407L;

    private Long id ;
    /** 资源名称 */
    private String sourceName ;
    /**
     * 资源code
     */
    private String sourceCode;

}
