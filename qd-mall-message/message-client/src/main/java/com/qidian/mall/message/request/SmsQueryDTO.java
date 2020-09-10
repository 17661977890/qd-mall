package com.qidian.mall.message.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: sunbin
 * @Description: 短信查询实体
 * @Date:
 * @Version: V1.0
 */
@Data
@ToString
public class SmsQueryDTO implements Serializable {
    private static final long serialVersionUID = -1862686577346987468L;

    /**
     * 业务id
     */
    private String bizId;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;
    /**
     * 发送日期
     */
    @NotBlank(message = "发送日期不能为空")
    private Date sendDate;

    /**
     * 分页参数
     */
    @NotBlank(message = "分页参数不能为空")
    private String pageSize;

    @NotBlank(message = "分页参数不能为空")
    private String currentPage;
}
