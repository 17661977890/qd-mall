package com.qidian.mall.user.mapper;

import com.qidian.mall.user.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.response.UserInfoVO;

/**
 * <p>
 * 系统用户名 Mapper 接口
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    UserInfoVO getUserInfo(String username);
}
