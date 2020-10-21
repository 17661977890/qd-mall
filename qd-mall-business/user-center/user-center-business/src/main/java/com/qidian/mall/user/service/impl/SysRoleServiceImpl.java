package com.qidian.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseServiceImpl;
import com.central.base.util.ConstantUtil;
import com.central.base.util.IdWorker;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.mapper.SysRoleMapper;
import com.qidian.mall.user.request.SysRoleDTO;
import com.qidian.mall.user.request.SysUserDTO;
import com.qidian.mall.user.response.SysRoleVo;
import com.qidian.mall.user.response.SysUserVO;
import com.qidian.mall.user.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理业务实现类
 * @Author binsun
 * @Date 2020-09-28
 * @Description
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends BaseServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 新增角色
     * @param sysRoleDTO
     * @return
     */
    @Override
    public Integer addRole(SysRoleDTO sysRoleDTO,String username) {
        // 角色名称和code 判重
        List<SysRole> sysRoleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>()
                .eq(SysRole.COL_ROLE_NAME,sysRoleDTO.getRoleName())
                .or()
                .eq(SysRole.COL_ROLE_CODE,sysRoleDTO.getRoleCode()));
        if(CollectionUtils.isNotEmpty(sysRoleList)){
            throw new BusinessException("102400",getMessage("102400"));
        }
        SysRole newRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO,newRole);
        newRole.setId(new IdWorker().nextId());
        addCommonField(newRole,username);
        int result = sysRoleMapper.insert(newRole);
        log.info("add new role error result:{}",result);
        if(result!=1){
            throw new BusinessException("102401",getMessage("102401"));
        }
        return result;
    }

    /**
     * 编辑角色
     * @param sysRoleDTO
     * @return
     */
    @Override
    public Integer updateRole(SysRoleDTO sysRoleDTO,String username) {
        // 角色名称和code 判重
        List<SysRole> sysRoleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>()
                        .ne(SysRole.COL_ID,sysRoleDTO.getId())
                        .and(i->i.eq(SysRole.COL_ROLE_NAME,sysRoleDTO.getRoleName()).or().eq(SysRole.COL_ROLE_CODE,sysRoleDTO.getRoleCode())));
        if(CollectionUtils.isNotEmpty(sysRoleList)){
            throw new BusinessException("102400",getMessage("102400"));
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO,role);
        updateCommonField(role,username);
        int result = sysRoleMapper.updateById(role);
        log.info("update role error result:{}",result);
        if(result!=1){
            throw new BusinessException("102402",getMessage("102402"));
        }
        return result;
    }

    /**
     * 关于逻辑删除不能自动填充更新字段的问题解决：
     *  LogicDeleteByIdWithFill 使用选装件
     *  mapper 添加方法：int deleteByIdWithFill(T entity);
     *  需要逻辑删除，同事更新其他字段，其他字段需要添加注解   @TableField(value = "update_user",fill = FieldFill.UPDATE)
     * @param id
     * @param username
     * @return
     */
    @Override
    public Integer deleteById(Long id, String username) {
        SysRole role = new SysRole();
        role.setId(id);
        updateCommonField(role,username);
        int result = sysRoleMapper.deleteByIdWithFill(role);
        log.info("logic delete role error result:{}",result);
        if(result!=1){
            throw new BusinessException("102404",getMessage("102404"));
        }
        return result;
    }

    /**
     * 查询角色详情
     * @param id
     * @return
     */
    @Override
    public SysRoleVo selectById(Long id) {
        SysRoleVo sysRoleVo = new SysRoleVo();
        SysRole sysRole = sysRoleMapper.selectById(id);
        BeanUtils.copyProperties(sysRole,sysRoleVo);
        return sysRoleVo;
    }

    /**
     * 条件查询角色
     * @param sysRoleDTO 查询请求条件
     * @return
     */
    @Override
    public List<SysRoleVo> selectAll(SysRoleDTO sysRoleDTO) {
        List<SysRole> sysRoleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>()
                .eq(StringUtils.isNotEmpty(sysRoleDTO.getRoleCode()),SysRole.COL_ROLE_CODE,sysRoleDTO.getRoleCode())
                .eq(StringUtils.isNotEmpty(sysRoleDTO.getRoleName()),SysRole.COL_ROLE_NAME,sysRoleDTO.getRoleName()));
        List<SysRoleVo> list = new ArrayList<>();
        for (SysRole sysRole:sysRoleList) {
            SysRoleVo sysRoleVo = new SysRoleVo();
            BeanUtils.copyProperties(sysRole,sysRoleVo);
            list.add(sysRoleVo);
        }
        return list;
    }

    /**
     * 分页条件查询角色
     * @param sysRoleDTO 查询请求条件
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public IPage<SysRoleVo> selectPage(SysRoleDTO sysRoleDTO, int pageSize, int pageNum) {
        Page<SysRole> page = new Page<>(pageNum,pageSize);
        // 查询条件
        QueryWrapper<SysRole> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(this.convertEntity(sysRoleDTO));
        IPage<SysRole> list = sysRoleMapper.selectPage(page, queryWrapper);
        IPage<SysRoleVo> iPage = new Page<>();
        iPage.setRecords(this.convert(list.getRecords()));
        iPage.setCurrent(list.getCurrent());
        iPage.setSize(list.getSize());
        iPage.setTotal(list.getTotal());
        iPage.setPages(list.getPages());
        return iPage;
    }



    /**
     * DTO -> Entity （copy 属性名和类型一致才可以转换）
     * @param dto 对象
     * @return Entity
     */
    private SysRole convertEntity(SysRoleDTO dto){
        SysRole data = new SysRole();
        BeanUtils.copyProperties(dto, data);
        return data;
    }

    /**
     * 批量：Entity -> VO （copy 属性名和类型一致才可以转换）
     * @param list 对象
     * @return VO对象
     */
    private List<SysRoleVo> convert(List<SysRole> list){
        List<SysRoleVo> sysRoleVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return sysRoleVoList;
        }
        for (SysRole source : list) {
            SysRoleVo target = new SysRoleVo();
            BeanUtils.copyProperties(source, target);
            sysRoleVoList.add(target);
        }
        return sysRoleVoList;
    }
}
