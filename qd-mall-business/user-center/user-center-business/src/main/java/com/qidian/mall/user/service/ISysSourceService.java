package com.qidian.mall.user.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.user.request.SysSourceDTO;
import com.qidian.mall.user.response.SysSourceVo;

import java.util.List;

/**
 * 资源管理接口
 * @Author binsun
 * @Date 2020-09-28
 * @Description
 */
public interface ISysSourceService {

    /**
     * 新增资源
     * @param sysSourceDTO
     * @return
     */
    Integer addSource(SysSourceDTO sysSourceDTO, String username);

    /**
     * 编辑资源
     * @param sysSourceDTO
     * @return
     */
    Integer updateSource(SysSourceDTO sysSourceDTO, String username);

    /**
     * 逻辑删除资源
     * @param id
     * @param username
     * @return
     */
    Integer deleteById(Long id, String username);

    /**
     * 查询资源详情
     * @param id
     * @return
     */
    SysSourceVo selectById(Long id);

    /**
     * 树查询
     * @param sysSourceDTO 查询请求条件
     * @return 信息列表
     */
    List<Object> getTreeList(SysSourceDTO sysSourceDTO);

    /**
     * 条件查询
     * @param sysSourceDTO
     * @return
     */
    List<SysSourceVo> getListBy(SysSourceDTO sysSourceDTO);

    /**
     * 分页查询信息对象
     * @param sysSourceDTO 查询请求条件
     * @return 信息列表
     */
    IPage<SysSourceVo> selectPage(SysSourceDTO sysSourceDTO, int pageSize, int pageNum);


    /**
     * 编辑显示状态
     * @param sysSourceDTO
     * @return
     */
    Integer updateShowStatus(SysSourceDTO sysSourceDTO, String username);


}
