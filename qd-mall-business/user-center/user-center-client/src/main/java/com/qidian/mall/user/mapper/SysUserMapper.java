package com.qidian.mall.user.mapper;

import com.qidian.mall.user.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qidian.mall.user.response.UserInfoVO;

import java.util.List;

/**
 * <p>
 * 系统用户名 Mapper 接口
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据用户名获取用户基本信息
     * @param username
     * @return
     */
    UserInfoVO getUserInfo(String username);

    /**
     * 根据角色id获取角色下的用户
     * @param roleId
     * @return
     */
    List<SysUser> getUserByRoleId(Long roleId);
}
