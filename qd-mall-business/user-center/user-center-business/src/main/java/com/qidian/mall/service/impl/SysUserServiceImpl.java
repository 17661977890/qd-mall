package com.qidian.mall.service.impl;

import com.central.base.message.MessageSourceService;
import com.central.base.util.PasswordEncoderUtil;
import com.qidian.mall.entity.CustomUserDetails;
import com.qidian.mall.entity.SysUser;
import com.qidian.mall.request.SysUserDTO;
import com.qidian.mall.response.SysUserVO;
import com.qidian.mall.mapper.SysUserMapper;
import com.qidian.mall.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
* <p>
 * SysUser服务实现类---单一继承的弊端
 * </p>
*
* @author binsun
* @since 2020-01-10
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private MessageSourceService e;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
    * 保存信息对象 ---JAVA8 新特性的时间属性为LocalDateTime
    * @param record
    * @return
    */
    @Override
    public Integer save(SysUserDTO record) {
        SysUser data = this.convertEntity(record);
        data.setCreateTime(new Date());
        return baseMapper.insert(data);
    }

    /**
    * 根据主键更新信息对象
    * @param record 信息对象
    * @return 影响记录数
    */
    @Override
    public Integer updateById(SysUserDTO record) {
        SysUser data = this.convertEntity(record);
        data.setUpdateTime(new Date());
        return baseMapper.updateById(data);
    }

    /**
    * 根据主键删除信息对象
    * 逻辑删除,字段改为删除态
    * @param id 主键
    * @return 影响记录数
    */
    @Override
    public Integer deleteById(String id) {
        return baseMapper.deleteById(id);
    }

    /**
    * 根据主键查询信息对象
    * @param id 主键
    * @return 信息对象
    */
    @Override
    public SysUserVO selectById(String id) {
        SysUser data = baseMapper.selectById(id);
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(data,vo);
        return vo;
    }

    /**
    * 根据条件查询信息对象
    * @param record 查询请求条件
    * @return 信息列表
    */
    @Override
    public List<SysUserVO> selectAll(SysUserDTO record) {
        SysUser data = this.convertEntity(record);
        QueryWrapper<SysUser> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(data);
        List<SysUser> list = baseMapper.selectList(queryWrapper);
        return this.convert(list);
    }

    /**
    * 分页查询
    * @param record 查询请求条件
    * @return 信息列表
    */
    @Override
    public IPage<SysUserVO> selectPage(SysUserDTO record) {
        // DTO -> entity 转换
        SysUser data = this.convertEntity(record);
        // 分页数据设置
        Page<SysUser> page = new Page<>(record.getCurrent(),record.getSize());
        // 查询条件
        QueryWrapper<SysUser> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(data);
        IPage<SysUser> list = baseMapper.selectPage(page, queryWrapper);
        IPage<SysUserVO> iPage = new Page<>();
        iPage.setRecords(this.convert(list.getRecords()));
        iPage.setCurrent(list.getCurrent());
        iPage.setSize(list.getSize());
        iPage.setTotal(list.getTotal());
        iPage.setPages(list.getPages());
        return iPage;
    }

    //========================== 授权认证相关接口 ============================

    @Override
    public CustomUserDetails findByUsername(String username) {
        SysUser sysUser = this.selectByUsername(username);
        return getLoginAppUser(sysUser);
    }

    @Override
    public CustomUserDetails findByMobile(String mobile) {
        SysUser sysUser = this.selectByMobile(mobile);
        return getLoginAppUser(sysUser);
    }

    @Override
    public CustomUserDetails findByOpenId(String openId) {
        SysUser sysUser = this.selectByOpenId(openId);
        return getLoginAppUser(sysUser);
    }

    private CustomUserDetails getLoginAppUser(SysUser sysUser) {
        CustomUserDetails loginAppUser = new CustomUserDetails();
        BeanUtils.copyProperties(sysUser, loginAppUser);
        //这里可以对CustomUserDetails 进一步做数据追加 角色 权限等信息
        return loginAppUser;
    }


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public SysUser selectByUsername(String username) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("username", username)
        );
        return getUser(users);
    }

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    @Override
    public SysUser selectByMobile(String mobile) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("mobile", mobile)
        );
        return getUser(users);
    }

    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    @Override
    public SysUser selectByOpenId(String openId) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("open_id", openId)
        );
        return getUser(users);
    }

    private SysUser getUser(List<SysUser> users) {
        SysUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }


    /**
     * DTO -> Entity （copy 属性名和类型一致才可以转换）
     * @param dto 对象
     * @return Entity
     */
     private SysUser convertEntity(SysUserDTO dto){
         SysUser data = new SysUser();
         BeanUtils.copyProperties(dto, data);
         return data;
     }

     /**
     * 批量：Entity -> VO （copy 属性名和类型一致才可以转换）
     * @param list 对象
     * @return VO对象
     */
     private List<SysUserVO> convert(List<SysUser> list){
         List<SysUserVO> sysUserList = new ArrayList<>();
         if (CollectionUtils.isEmpty(list)) {
             return sysUserList;
         }
         for (SysUser source : list) {
             SysUserVO target = new SysUserVO();
             BeanUtils.copyProperties(source, target);
             sysUserList.add(target);
         }
         return sysUserList;
     }

}
