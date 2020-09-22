package com.qidian.mall.user.service.impl;

import com.central.base.exception.BusinessException;
import com.central.base.message.MessageSourceService;
import com.central.base.util.ConstantUtil;
import com.central.base.util.IdWorker;
import com.qidian.mall.user.entity.CustomUserDetails;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.enums.UserTypeEnum;
import com.qidian.mall.user.request.RegUserDTO;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import com.qidian.mall.user.request.SysUserDTO;
import com.qidian.mall.user.response.SysUserVO;
import com.qidian.mall.user.mapper.SysUserMapper;
import com.qidian.mall.user.service.ISysSmsService;
import com.qidian.mall.user.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Autowired
    private ISysSmsService iSysSmsService;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     *  新用户注册
     * @param regUserDTO
     * @return
     */
    @Override
    public boolean regUser(RegUserDTO regUserDTO) {
        // 验证验证码
        SysSmsCodeDTO sysSmsCodeDTO = new SysSmsCodeDTO();
        sysSmsCodeDTO.setPlatformType(regUserDTO.getPlatformType());
        sysSmsCodeDTO.setBusinessType(regUserDTO.getBusinessType());
        sysSmsCodeDTO.setReceiveTerminalNo(regUserDTO.getMobile());
        sysSmsCodeDTO.setReceiveTerminalType(regUserDTO.getReceiveTerminalType());
        sysSmsCodeDTO.setVerificationCode(regUserDTO.getSmsCode());
        sysSmsCodeDTO.setSmsCodeId(regUserDTO.getSmsCodeId());
        iSysSmsService.verifyCode(sysSmsCodeDTO);

        // 用户注册（新增用户）--- 密码和用户检查
        if(!regUserDTO.getPassword().equals(regUserDTO.getConfirmPassword())){
            throw new BusinessException("102320",e.getMessage("102320"));
        }
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq(SysUser.COL_USERNAME,regUserDTO.getUsername()));
        if(sysUser!=null){
            throw new BusinessException("102321",e.getMessage("102321"));
        }
        SysUser newUser = new SysUser();
        newUser.setId(new IdWorker().nextId());
        newUser.setUsername(regUserDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(regUserDTO.getPassword()));
        newUser.setMobile(regUserDTO.getMobile());
        newUser.setEnabled(ConstantUtil.DELETE_FLAG_Y);
        // web端注册为商家，移动端为app用户
        if(3==regUserDTO.getPlatformType()){
            newUser.setType(UserTypeEnum.MERCHANT.getCode());
            newUser.setNickname(UserTypeEnum.MERCHANT.getCode()+regUserDTO.getUsername());
        }else {
            newUser.setType(UserTypeEnum.APP.getCode());
            newUser.setNickname(UserTypeEnum.APP.getCode()+regUserDTO.getUsername());
        }
        newUser.setClientIp(regUserDTO.getClientIp());
        int result = sysUserMapper.insert(newUser);
        if(result!=1){
            throw new BusinessException("102322",e.getMessage("102322"));
        }
        return true;
    }

     // =================================================== 业务接口 =====================================
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
    public IPage<SysUserVO> selectPage(SysUserDTO record,int pageSize,int pageNum) {
        // DTO -> entity 转换
        SysUser data = this.convertEntity(record);
        // 分页数据设置
        Page<SysUser> page = new Page<>(pageNum,pageSize);
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

    // ===================================== 授权认证相关接口 ========================================

    /**
     *  用户密码登录
     * @param username
     * @return
     */
    @Override
    public CustomUserDetails findByUsername(String username) {
        SysUser sysUser = this.selectByUsername(username);
        if(sysUser==null){
            throw new BusinessException("102201",e.getMessage("102201"));
        }
        return getLoginAppUser(sysUser);
    }

    /**
     * 手机号 密码登录
     * @param mobile
     * @return
     */
    @Override
    public CustomUserDetails findByMobile(String mobile) {
        SysUser sysUser = this.selectByMobile(mobile);
        return getLoginAppUser(sysUser);
    }

    /**
     * openId 登录
     * @param openId
     * @return
     */
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
