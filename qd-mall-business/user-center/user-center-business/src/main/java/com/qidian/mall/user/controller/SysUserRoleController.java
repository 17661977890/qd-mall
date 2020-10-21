package com.qidian.mall.user.controller;

import com.central.base.mvc.BaseController;

import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.request.UserRoleDTO;
import com.qidian.mall.user.service.ISysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 系统角色关系 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Slf4j
@Api(tags = {"用户角色关系管理"})
@RestController
@RequestMapping("/sys-user-role")
public class SysUserRoleController extends BaseController {

    @Autowired
    public ISysUserRoleService sysUserRoleService;

     
    /**
    * 保存用户角色关系
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "保存用户角色关系", notes = "保存用户角色关系")
    @RequestMapping(value = "/saveUserRole", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse saveUserRole(@Validated @RequestBody RestRequest<UserRoleDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal(); 
        Boolean result = sysUserRoleService.saveUserRole(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }



    /**
    * 查询用户拥有的角色
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "查询用户拥有的角色", notes = "查询用户拥有的角色")
    @RequestMapping(value = "/queryRoleByUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse queryRoleByUser(@RequestBody RestRequest<UserRoleDTO> restRequest) throws InterruptedException {
        List<SysRole> result = sysUserRoleService.getRoleByUserId(restRequest.getBody().getUserId());
        return new RestResponse().success(result);
    }


}
