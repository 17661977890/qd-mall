package com.qidian.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseServiceImpl;
import com.central.base.util.IdWorker;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.mapper.SysSourceMapper;
import com.qidian.mall.user.request.SysSourceDTO;
import com.qidian.mall.user.response.SysSourceTreeVo;
import com.qidian.mall.user.response.SysSourceVo;
import com.qidian.mall.user.service.ISysSourceService;
import com.qidian.mall.user.service.utils.SysSourceTreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源管理业务实现类
 * @Author binsun
 * @Date 2020-09-28
 * @Description
 */
@Slf4j
@Service
public class SysSourceServiceImpl extends BaseServiceImpl implements ISysSourceService {

    @Autowired
    private SysSourceMapper sysSourceMapper;

    /**
     * 新增资源
     * @param sysSourceDTO
     * @return
     */
    @Override
    public Integer addSource(SysSourceDTO sysSourceDTO,String username) {
        // 资源名称和code 判重
        List<SysSource> sysSourceList = sysSourceMapper.selectList(new QueryWrapper<SysSource>()
                .eq(SysSource.COL_SOURCE_NAME,sysSourceDTO.getSourceName()));
        if(CollectionUtils.isNotEmpty(sysSourceList)){
            throw new BusinessException("102500",getMessage("102500"));
        }
        SysSource newSource = new SysSource();
        BeanUtils.copyProperties(sysSourceDTO,newSource);
        newSource.setId(new IdWorker().nextId());
        if(sysSourceDTO.getParentId()==null){
            newSource.setParentId(0L);
        }
        addCommonField(newSource,username);
        int result = sysSourceMapper.insert(newSource);
        log.info("add new Source error result:{}",result);
        if(result!=1){
            throw new BusinessException("102501",getMessage("102501"));
        }
        return result;
    }

    /**
     * 编辑资源
     * @param sysSourceDTO
     * @return
     */
    @Override
    public Integer updateSource(SysSourceDTO sysSourceDTO,String username) {
        // 资源名称和code 判重
        List<SysSource> sysSourceList = sysSourceMapper.selectList(new QueryWrapper<SysSource>()
                        .ne(SysSource.COL_ID,sysSourceDTO.getId())
                        .and(i->i.eq(SysSource.COL_SOURCE_NAME,sysSourceDTO.getSourceName())));
        if(CollectionUtils.isNotEmpty(sysSourceList)){
            throw new BusinessException("102500",getMessage("102500"));
        }
        SysSource Source = new SysSource();
        BeanUtils.copyProperties(sysSourceDTO,Source);
        updateCommonField(Source,username);
        int result = sysSourceMapper.updateById(Source);
        log.info("update Source error result:{}",result);
        if(result!=1){
            throw new BusinessException("102502",getMessage("102502"));
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
        SysSource Source = new SysSource();
        Source.setId(id);
        updateCommonField(Source,username);
        int result = sysSourceMapper.deleteByIdWithFill(Source);
        log.info("logic delete Source error result:{}",result);
        if(result!=1){
            throw new BusinessException("102504",getMessage("102504"));
        }
        return result;
    }

    /**
     * 查询资源详情
     * @param id
     * @return
     */
    @Override
    public SysSourceVo selectById(Long id) {
        SysSourceVo sysSourceVo = new SysSourceVo();
        SysSource sysSource = sysSourceMapper.selectById(id);
        BeanUtils.copyProperties(sysSource,sysSourceVo);
        return sysSourceVo;
    }

    /**
     * 条件查询资源树
     * @param sysSourceDTO 查询请求条件
     * @return
     */
    @Override
    public List<Object> getTreeList(SysSourceDTO sysSourceDTO) {
        List<SysSource> sysSourceList = sysSourceMapper.selectList(new QueryWrapper<SysSource>()
                .eq(sysSourceDTO.getSourceType()!=null,SysSource.COL_SOURCE_TYPE,sysSourceDTO.getSourceType())
                .eq(StringUtils.isNotEmpty(sysSourceDTO.getSourceName()),SysSource.COL_SOURCE_NAME,sysSourceDTO.getSourceName()));
        List<SysSourceTreeVo> list = new ArrayList<>();
        for (SysSource sysSource:sysSourceList) {
            SysSourceTreeVo sysSourceTreeVo = new SysSourceTreeVo();
            BeanUtils.copyProperties(sysSource,sysSourceTreeVo);
            list.add(sysSourceTreeVo);
        }
        SysSourceTreeUtils sysSourceTreeUtils = new SysSourceTreeUtils();
        List<Object> treeList = sysSourceTreeUtils.treeSource(list);
        return treeList;
    }

    /**
     * 条件查询资源 (根据资源类型筛选)
     * @param sysSourceDTO 查询请求条件
     * @return
     */
    @Override
    public List<SysSourceVo> getListBy(SysSourceDTO sysSourceDTO) {
        List<SysSource> sysSourceList = sysSourceMapper.selectList(new QueryWrapper<SysSource>()
                .eq(sysSourceDTO.getSourceType()!=null,SysSource.COL_SOURCE_TYPE,sysSourceDTO.getSourceType())
                .eq(StringUtils.isNotEmpty(sysSourceDTO.getSourceName()),SysSource.COL_SOURCE_NAME,sysSourceDTO.getSourceName()));
        List<SysSourceVo> list = new ArrayList<>();
        for (SysSource sysSource:sysSourceList) {
            SysSourceVo sysSourceVo = new SysSourceVo();
            BeanUtils.copyProperties(sysSource,sysSourceVo);
            list.add(sysSourceVo);
        }
        return list;
    }

    /**
     * 分页条件查询资源
     * @param sysSourceDTO 查询请求条件
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public IPage<SysSourceVo> selectPage(SysSourceDTO sysSourceDTO, int pageSize, int pageNum) {
        Page<SysSource> page = new Page<>(pageNum,pageSize);
        // 查询条件
        QueryWrapper<SysSource> queryWrapper =  new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sysSourceDTO.getSourceName()),SysSource.COL_SOURCE_NAME,sysSourceDTO.getSourceName())
                .eq(sysSourceDTO.getSourceType()!=null,SysSource.COL_SOURCE_TYPE, sysSourceDTO.getSourceType());
//        queryWrapper.setEntity(this.convertEntity(sysSourceDTO));
        IPage<SysSource> list = sysSourceMapper.selectPage(page, queryWrapper);
        IPage<SysSourceVo> iPage = new Page<>();
        iPage.setRecords(this.convert(list.getRecords()));
        iPage.setCurrent(list.getCurrent());
        iPage.setSize(list.getSize());
        iPage.setTotal(list.getTotal());
        iPage.setPages(list.getPages());
        return iPage;
    }

    /**
     * 编辑显示状态
     * @param sysSourceDTO
     * @param username
     * @return
     */
    @Override
    public Integer updateShowStatus(SysSourceDTO sysSourceDTO, String username) {
        SysSource oldSource = sysSourceMapper.selectById(sysSourceDTO.getId());
        if(oldSource==null) {
            throw new BusinessException("102503", getMessage("102503"));
        }
        SysSource newSource = new SysSource();
        newSource.setId(sysSourceDTO.getId());
        newSource.setShowFlag(sysSourceDTO.getShowFlag());
        updateCommonField(newSource,username);
        int result = sysSourceMapper.updateById(newSource);
        log.info("update Source error result:{}",result);
        if(result!=1){
            throw new BusinessException("102502",getMessage("102502"));
        }
        return result;
    }


    /**
     * DTO -> Entity （copy 属性名和类型一致才可以转换）
     * @param dto 对象
     * @return Entity
     */
    private SysSource convertEntity(SysSourceDTO dto){
        SysSource data = new SysSource();
        BeanUtils.copyProperties(dto, data);
        return data;
    }

    /**
     * 批量：Entity -> VO （copy 属性名和类型一致才可以转换）
     * @param list 对象
     * @return VO对象
     */
    private List<SysSourceVo> convert(List<SysSource> list){
        List<SysSourceVo> sysSourceVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return sysSourceVoList;
        }
        for (SysSource source : list) {
            SysSourceVo target = new SysSourceVo();
            BeanUtils.copyProperties(source, target);
            sysSourceVoList.add(target);
        }
        return sysSourceVoList;
    }
}
