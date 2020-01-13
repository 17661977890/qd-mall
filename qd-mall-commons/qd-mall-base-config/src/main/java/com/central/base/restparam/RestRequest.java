package com.central.base.restparam;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 入参规范类
 * @Author 彬
 * @Date 2019/4/19
 */
@Data
public class RestRequest<T> implements Serializable{
    private static final long serialVersionUID = -4582153239301805627L;

    private RestRequestHeader header;
    /**
     * 加注解，保证参数校验有效
     */
    @Valid
    private T body;
}
