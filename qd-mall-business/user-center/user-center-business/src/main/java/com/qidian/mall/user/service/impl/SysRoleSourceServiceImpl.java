package com.qidian.mall.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.base.mvc.BaseServiceImpl;
import com.central.base.util.IdWorker;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysRoleSource;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.mapper.SysRoleMapper;
import com.qidian.mall.user.mapper.SysRoleSourceMapper;
import com.qidian.mall.user.mapper.SysSourceMapper;
import com.qidian.mall.user.request.RoleSourceDTO;
import com.qidian.mall.user.service.ISysRoleSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 角色资源关系业务实现
 * @Author binsun
 * @Date 2020-10-21
 * @Description
 */
@Slf4j
@Service
public class SysRoleSourceServiceImpl extends BaseServiceImpl implements ISysRoleSourceService {

    @Autowired
    private SysRoleSourceMapper sysRoleSourceMapper;

    @Autowired
    private SysSourceMapper sysSourceMapper;


    /**
     * 批量保存角色资源关系实体
     * @param roleSourceDTO
     * @return
     */
    @Transactional
    @Override
    public Boolean saveRoleSource(RoleSourceDTO roleSourceDTO,String username) {
        // 1、查用户之前有的角色
        List<SysRoleSource> sysRoleSourceList = sysRoleSourceMapper.selectList(new QueryWrapper<SysRoleSource>().eq(SysRoleSource.COL_ROLE_ID,roleSourceDTO.getRoleId()));
        // 组装原始和现传得 roleId 集合
        List<Long> oldSourceIdList = sysRoleSourceList.stream().map(SysRoleSource::getSourceId).collect(Collectors.toList());
        List<Long> newSourceIdList = roleSourceDTO.getSourceIdList();

        // 2、编辑角色（传递为空、不为空（全有id、部分有id、全无id））
        if(CollectionUtils.isEmpty(newSourceIdList)){
            //传递为空,原有不为空，删除用户的所有角色
            if(CollectionUtil.isNotEmpty(sysRoleSourceList)){
                int result = sysRoleSourceMapper.delete(new QueryWrapper<SysRoleSource>().eq(SysRoleSource.COL_ROLE_ID,roleSourceDTO.getRoleId()));
                log.info("role source edit,sourceId list param is empty ---> has deleted count：{}",result);
            }
        }else {
            //传递不为空，（取差集，没有变化不操作，少了删除,多了新增）
            List<Long> reduceSourceIdList = oldSourceIdList.stream().filter(item -> !newSourceIdList.contains(item)).collect(toList());
            List<Long> addSourceIdList = newSourceIdList.stream().filter(item -> !oldSourceIdList.contains(item)).collect(toList());
            int deleteCount=0;
            for (Long sourceId:reduceSourceIdList) {
                sysRoleSourceMapper.delete(new QueryWrapper<SysRoleSource>().eq(SysRoleSource.COL_ROLE_ID,roleSourceDTO.getRoleId()).eq(SysRoleSource.COL_SOURCE_ID,sourceId));
                deleteCount++;
            }
            int result=0;
            List<SysRoleSource> addList = new ArrayList<>();
            for (Long sourceId:addSourceIdList) {
                SysRoleSource sysRoleSource = new SysRoleSource();
                sysRoleSource.setId(new IdWorker().nextId());
                sysRoleSource.setSourceId(sourceId);
                sysRoleSource.setRoleId(roleSourceDTO.getRoleId());
                addList.add(sysRoleSource);
            }
            if(!CollectionUtils.isEmpty(addList)){
                result = sysRoleSourceMapper.batchInsert(addList);
            }

            log.info("role source edit,sourceId list param is not empty ----> has delete count:{},has add count:{}",deleteCount,result);
        }
        return true;
    }

    /**
     * 获取角色得资源列表
     * @param roleId
     * @return
     */
    @Override
    public List<SysSource> getSourceListByRoleId(Long roleId) {
        return sysSourceMapper.getSourceListByRoleId(roleId);
    }

    @Override
    public List<SysSource> getSourceListByRoleIds(List<Long> roleIdList) {
        List<SysSource> sourceList = sysSourceMapper.getSourceListByRoleIds(roleIdList);
        sourceList = sourceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(SysSource::getId))), ArrayList::new));
        return sourceList;
    }

    /**
     * 获取用户得资源列表
     * @param userId
     * @return
     */
    @Override
    public List<SysSource> getSourceListByUserId(Long userId) {
        return sysSourceMapper.getSourceListByUserId(userId);
    }

}
