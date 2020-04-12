package com.central.base.restparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 出参头部
 * @Author 彬
 * @Date 2019/4/19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestResponseHeader implements Serializable{

    private static final long serialVersionUID = 4162338550181764762L;

    private String code;
    private String message;
}
