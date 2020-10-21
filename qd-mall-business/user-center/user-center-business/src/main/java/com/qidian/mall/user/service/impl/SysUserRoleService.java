package com.qidian.mall.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.base.mvc.BaseServiceImpl;
import com.central.base.util.IdWorker;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysUserRole;
import com.qidian.mall.user.mapper.SysRoleMapper;
import com.qidian.mall.user.mapper.SysUserRoleMapper;
import com.qidian.mall.user.request.UserRoleDTO;
import com.qidian.mall.user.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 用户角色关系业务实现
 * @Author binsun
 * @Date 2020-10-21
 * @Description
 */
@Slf4j
@Service
public class SysUserRoleService extends BaseServiceImpl implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;


    /**
     * 批量保存用户角色关系实体
     * @param userRoleDTO
     * @return
     */
    @Transactional
    @Override
    public Boolean saveUserRole(UserRoleDTO userRoleDTO,String username) {
        // 1、查用户之前有的角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq(SysUserRole.COL_USER_ID,userRoleDTO.getUserId()));
        // 组装原始和现传得 roleId 集合
        List<Long> oldRoleIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<Long> newRoleIdList = userRoleDTO.getRoleIdList();

        // 2、编辑角色（传递为空、不为空（全有id、部分有id、全无id））
        if(CollectionUtils.isEmpty(newRoleIdList)){
            //传递为空,原有不为空，删除用户的所有角色
            if(CollectionUtil.isNotEmpty(sysUserRoleList)){
                int result = sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq(SysUserRole.COL_USER_ID,userRoleDTO.getUserId()));
                log.info("user role edit,roleId list param is empty ---> has deleted count：{}",result);
            }
        }else {
            //传递不为空，（取差集，没有变化不操作，少了删除,多了新增）
            List<Long> reduceRoleIdList = oldRoleIdList.stream().filter(item -> !newRoleIdList.contains(item)).collect(toList());
            List<Long> addRoleIdList = newRoleIdList.stream().filter(item -> !oldRoleIdList.contains(item)).collect(toList());
            int deleteCount=0;
            for (Long roleId:reduceRoleIdList) {
                sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq(SysUserRole.COL_USER_ID,userRoleDTO.getUserId()).eq(SysUserRole.COL_ROLE_ID,roleId));
                deleteCount++;
            }
            int result=0;
            List<SysUserRole> addList = new ArrayList<>();
            for (Long roleId:addRoleIdList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(new IdWorker().nextId());
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userRoleDTO.getUserId());
                addList.add(sysUserRole);
            }
            if(!CollectionUtils.isEmpty(addList)){
                result = sysUserRoleMapper.batchInsert(addList);
            }

            log.info("user role edit,roleId list param is not empty ----> has delete count:{},has add count:{}",deleteCount,result);
        }
        return true;
    }

    /**
     * 根据用户id查询角色信息列表
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        return sysRoleMapper.getRoleByUserId(userId);
    }
}
