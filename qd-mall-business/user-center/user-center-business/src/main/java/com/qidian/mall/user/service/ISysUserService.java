package com.qidian.mall.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.user.entity.CustomUserDetails;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.request.RegUserDTO;
import com.qidian.mall.user.request.SysUserDTO;
import com.qidian.mall.user.response.SysUserVO;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* <p>
 * SysUser服务类
 * </p>
*
* @author binsun
* @since 2020-01-10
*/
public interface ISysUserService extends IService<SysUser> {


    /**
     * 新用户注册
     * @param regUserDTO
     * @return
     */
    boolean regUser(RegUserDTO regUserDTO);


    /**
     *  ================================================== 业务接口 ========================================
     */
    /**
    * 保存信息对象
    * @param record 信息对象
    * @return 影响记录数
    */
    Integer save(SysUserDTO record);

    /**
    * 根据主键更新信息对象
    * @param record 信息对象
    * @return 影响记录数
    */
    Integer updateById(SysUserDTO record);

    /**
    * 根据主键删除信息对象
    * 逻辑删除,字段改为删除态
    * @param id 主键
    * @return 影响记录数
    */
    Integer deleteById(String id);

    /**
    * 根据主键查询信息对象
    * @param id 主键
    * @return 信息对象
    */
    SysUserVO selectById(String id);

    /**
    * 根据条件查询信息对象
    * @param record 查询请求条件
    * @return 信息列表
    */
    List<SysUserVO> selectAll(SysUserDTO record);

    /**
    * 分页查询信息对象
    * @param record 查询请求条件
    * @return 信息列表
    */
    IPage<SysUserVO> selectPage(SysUserDTO record,int pageSize,int pageNum);


    /**
     * ================================================登录相关接口============================================
     */

    CustomUserDetails findByUsername(String username);

    CustomUserDetails findByMobile(String mobile);

    CustomUserDetails findByOpenId(String openId);


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    SysUser selectByUsername(String username);
    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    SysUser selectByMobile(String mobile);
    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    SysUser selectByOpenId(String openId);

}
