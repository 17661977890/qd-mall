package com.qidian.mall.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统用户名
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="系统用户名")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    /**
     * Y:启用 N：未启用
     */
    private String enabled;

    @ApiModelProperty(value = "类型")
    private String type;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "open_id")
    private String openId;

    @TableLogic
    @ApiModelProperty(value = "删除标识")
    private String deleteFlag;

    @ApiModelProperty(value = "用户注册来源ip")
    private String clientIp;

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_NICKNAME = "nickname";

    public static final String COL_HEAD_IMG_URL = "head_img_url";

    public static final String COL_MOBILE = "mobile";

    public static final String COL_CLIENT_IP = "client_ip";

    public static final String COL_SEX = "sex";

    public static final String COL_ENABLED = "enabled";

    public static final String COL_TYPE = "type";

    public static final String COL_COMPANY = "company";

    public static final String COL_OPEN_ID = "open_id";


    public static final String COL_DELETE_FLAG = "delete_flag";

    public static final String COL_VERSION = "version";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";



}
