package com.qidian.mall.user.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.user.request.SysRoleDTO;
import com.qidian.mall.user.response.SysRoleVo;

import java.util.List;

/**
 * 角色管理接口
 * @Author binsun
 * @Date 2020-09-28
 * @Description
 */
public interface ISysRoleService {

    /**
     * 新增角色
     * @param sysRoleDTO
     * @return
     */
    Integer addRole(SysRoleDTO sysRoleDTO,String username);

    /**
     * 编辑角色
     * @param sysRoleDTO
     * @return
     */
    Integer updateRole(SysRoleDTO sysRoleDTO,String username);

    /**
     * 逻辑删除角色
     * @param id
     * @param username
     * @return
     */
    Integer deleteById(Long id,String username);

    /**
     * 查询角色详情
     * @param id
     * @return
     */
    SysRoleVo selectById(Long id);

    /**
     * 根据条件查询信息对象
     * @param sysRoleDTO 查询请求条件
     * @return 信息列表
     */
    List<SysRoleVo> selectAll(SysRoleDTO sysRoleDTO);

    /**
     * 分页查询信息对象
     * @param sysRoleDTO 查询请求条件
     * @return 信息列表
     */
    IPage<SysRoleVo> selectPage(SysRoleDTO sysRoleDTO, int pageSize, int pageNum);


}
