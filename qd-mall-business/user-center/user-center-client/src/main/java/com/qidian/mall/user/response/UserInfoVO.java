package com.qidian.mall.user.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 登录用户信息接口
 * @author sunbin
 * @date 2021-04-245
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 8335458566019218012L;

    // =============================  用户基本信息 ======================
    @ApiModelProperty(value = "主键id")
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

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "open_id")
    private String openId;

    @ApiModelProperty(value = "用户注册来源ip")
    private Boolean clientIp;

    @ApiModelProperty(value = "邮箱")
    private String email;

    // ============================== 角色信息 =====================
    @ApiModelProperty(value = "角色列表")
    private List<SysRoleVo> roleVoList;
}
