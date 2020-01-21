package com.central.base.restparam;

import com.central.base.util.ConstantUtil;
import lombok.Data;

import java.io.Serializable;

/**
 *  出参规范类---------出参建议不事先确定泛型，业务层出现遗产捕获后，会报一个错，返回泛型json不对应
 * @Author 彬
 * @Date 2019/4/19
 */
@Data
public class RestResponse<T> implements Serializable {
    private static final long serialVersionUID = 4211323659331327447L;

    private RestResponseHeader header;
    private T body;

    /**
     * 成功返回处理
     * @param body
     * @return
     */
    public RestResponse success(T body) {
        RestResponseHeader restResponseHeader = new RestResponseHeader();
        restResponseHeader.setCode(ConstantUtil.SUCCESS);
        restResponseHeader.setMessage("SUCCESS!");
        this.header = restResponseHeader;
        this.body = body;
        return this;
    }
    public static RestResponse resultSuccess(Object data) {
        return new RestResponse().success(data);
    }

    /**
     * 失败返回处理----全局异常处理不用，不能返回对应json消息串
     * @param code
     * @param msg
     * @return
     */
    public static RestResponse resultError(String code, String msg) {
        return new RestResponse().error(code, msg);
    }

    public RestResponse error(String code, String msg) {
        return this.error(code, msg, null);
    }

    public RestResponse error(String code, String msg, T body) {
        RestResponseHeader restResponseHeader = new RestResponseHeader();
        restResponseHeader.setCode(code);
        restResponseHeader.setMessage(msg);
        this.setHeader(restResponseHeader);
        this.body = body;
        return this;
    }
}
