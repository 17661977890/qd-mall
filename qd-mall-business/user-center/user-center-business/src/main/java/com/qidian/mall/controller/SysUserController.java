package com.qidian.mall.controller;

import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.request.SysUserDTO;
import com.qidian.mall.response.SysUserVO;
import com.qidian.mall.service.ISysUserService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统用户名 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Api(tags = {"系统用户名web层"})
@RestController
@RequestMapping("/sys-user")
public class SysUserController extends BaseController {

    @Autowired
    public ISysUserService sysUserService;

    /**
    * 新增系统用户名数据
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "保存", notes = "保存数据到SysUser")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse addSysUser(@RequestBody RestRequest<SysUserDTO> restRequest) {
        Integer result = sysUserService.save(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 更新系统用户名(根据主键id更新)
    * @param restRequest 修改参数
    * @return 是否更改成功
    */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新SysUser数据")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse updateSysUserById(@RequestBody RestRequest<SysUserDTO> restRequest) {
        Integer result = sysUserService.updateById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 删除系统用户名(根据主键id伪删除)
    * @param restRequest 主键id
    * @return 是否删除成功
    */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除SysUser数据")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse deleteSysUserById(@RequestBody RestRequest<String> restRequest) {
        Integer result = sysUserService.deleteById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 根据主键id查询单条系统用户名
    * @param restRequest 主键id
    * @return 查询结果
    */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取SysUser数据")
    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserById(@RequestBody RestRequest<String> restRequest) {
        SysUserVO result = sysUserService.selectById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 查询全部系统用户名
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "全部查询", notes = "查询SysUser全部数据")
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserAll(@RequestBody RestRequest<SysUserDTO> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        List<SysUserVO> result = sysUserService.selectAll(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 分页查询系统用户名
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "分页查询", notes = "分页查询SysUser全部数据")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserPage(@RequestBody RestRequest<SysUserDTO> restRequest) {
        IPage<SysUserVO> result = sysUserService.selectPage(restRequest.getBody());
        return new RestResponse().success(result);
    }

}
