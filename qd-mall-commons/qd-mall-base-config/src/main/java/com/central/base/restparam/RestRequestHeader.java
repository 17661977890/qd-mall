package com.central.base.restparam;

import lombok.Data;

import java.io.Serializable;

/**
 * 入参头部
 * @Author 彬
 * @Date 2019/4/19
 */
@Data
public class RestRequestHeader implements Serializable {
    private static final long serialVersionUID = -6191845065673359053L;

    private String code;
    private String message;
    //当前页数
    private Integer pageNum;
    //当前页显示数量
    private Integer pageSize;
}
