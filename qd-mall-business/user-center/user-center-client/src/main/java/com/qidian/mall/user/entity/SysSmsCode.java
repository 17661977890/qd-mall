package com.qidian.mall.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 消息验证码实体
 */
@Data
@TableName(value = "sys_sms_code")
public class SysSmsCode {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 接收终端类型 1：手机 2：邮箱
     */
    @TableField(value = "receive_terminal_type")
    private Integer receiveTerminalType;

    /**
     * 接收终端号码 手机号/邮箱号
     */
    @TableField(value = "receive_terminal_no")
    private String receiveTerminalNo;

    /**
     * 过期时间
     */
    @TableField(value = "expired_time")
    private Date expiredTime;

    /**
     * 验证码
     */
    @TableField(value = "verification_code")
    private String verificationCode;

    /**
     * 业务类型 1：注册 2 登录 3 重置密码
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 客户端ip
     */
    @TableField(value = "client_ip")
    private String clientIp;

    /**
     * 发送平台类型 1：ios 2 android 3 web
     */
    @TableField(value = "platform_type")
    private Integer platformType;

    /**
     * 是否发送 Y、N
     */
    @TableField(value = "is_send")
    private String isSend;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 是否使用 Y、N
     */
    @TableField(value = "is_used")
    private String isUsed;

    /**
     * 使用时间
     */
    @TableField(value = "use_time")
    private Date useTime;

    /**
     * 验证时间
     */
    @TableField(value = "verify_time")
    private Date verifyTime;

    /**
     * 验证结果code
     */
    @TableField(value = "verify_code")
    private String verifyCode;

    /**
     * 验证信息
     */
    @TableField(value = "verify_message")
    private String verifyMessage;

    /**
     * 删除标识 Y、N
     */
    @TableLogic
    @TableField(value = "delete_flag")
    private String deleteFlag;

    /**
     * 乐观锁
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public static final String COL_ID = "id";

    public static final String COL_RECEIVE_TERMINAL_TYPE = "receive_terminal_type";

    public static final String COL_RECEIVE_TERMINAL_NO = "receive_terminal_no";

    public static final String COL_EXPIRED_TIME = "expired_time";

    public static final String COL_VERIFICATION_CODE = "verification_code";

    public static final String COL_BUSINESS_TYPE = "business_type";

    public static final String COL_CLIENT_IP = "client_ip";

    public static final String COL_PLATFORM_TYPE = "platform_type";

    public static final String COL_IS_SEND = "is_send";

    public static final String COL_SEND_TIME = "send_time";

    public static final String COL_IS_USED = "is_used";

    public static final String COL_USE_TIME = "use_time";

    public static final String COL_VERIFY_TIME = "verify_time";

    public static final String COL_VERIFY_CODE = "verify_code";

    public static final String COL_VERIFY_MESSAGE = "verify_message";

    public static final String COL_DELETE_FLAG = "delete_flag";

    public static final String COL_VERSION = "version";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";


}