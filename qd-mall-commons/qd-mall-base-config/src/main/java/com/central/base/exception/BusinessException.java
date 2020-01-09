package com.central.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 自定义异常类
 * @Author 彬
 * @Date 2019/4/19
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 4169965729609738000L;

    private String code;
    private String message;

    public BusinessException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }
}
