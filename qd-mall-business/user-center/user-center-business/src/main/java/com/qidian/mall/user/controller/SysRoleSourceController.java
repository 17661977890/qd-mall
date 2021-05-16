package com.qidian.mall.user.controller;

import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.entity.SysRole;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.request.RoleSourceDTO;
import com.qidian.mall.user.request.UserRoleDTO;
import com.qidian.mall.user.service.ISysRoleSourceService;
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
 * 系统角色资源关系 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Slf4j
@Api(tags = {"角色资源关系管理"})
@RestController
@RequestMapping("/sys-role-source")
public class SysRoleSourceController extends BaseController {

    @Autowired
    public ISysRoleSourceService sysRoleSourceService;

     
    /**
    * 保存角色资源关系
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "保存角色资源关系", notes = "保存角色资源关系")
    @RequestMapping(value = "/saveRoleSource", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse saveRoleSource(@Validated(RoleSourceDTO.Add.class) @RequestBody RestRequest<RoleSourceDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal(); 
        Boolean result = sysRoleSourceService.saveRoleSource(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }



    /**
    * 查询角色拥有资源
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "查询角色拥有资源", notes = "查询角色拥有资源")
    @RequestMapping(value = "/querySourceByRole", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse querySourceByRole(@RequestBody @Validated(RoleSourceDTO.RoleSource.class) RestRequest<RoleSourceDTO> restRequest) {
        List<SysSource> result = sysRoleSourceService.getSourceListByRoleId(restRequest.getBody().getRoleId());
        return new RestResponse().success(result);
    }


    /**
     * 查询角色拥有资源
     * @param restRequest 查询条件
     * @return 查询结果
     */
    @ApiOperation(value = "查询角色拥有资源", notes = "查询角色拥有资源")
    @RequestMapping(value = "/querySourceByRoles", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse querySourceByRoles(@RequestBody @Validated(RoleSourceDTO.RoleListSource.class) RestRequest<RoleSourceDTO> restRequest) {
        List<SysSource> result = sysRoleSourceService.getSourceListByRoleIds(restRequest.getBody().getRoleIds());
        return new RestResponse().success(result);
    }

    /**
     * 查询用户拥有资源
     * @param restRequest 查询条件
     * @return 查询结果
     */
    @ApiOperation(value = "查询用户拥有资源", notes = "查询用户拥有资源")
    @RequestMapping(value = "/querySourceByUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse querySourceByUser(@RequestBody @Validated(RoleSourceDTO.UserSource.class) RestRequest<RoleSourceDTO> restRequest)  {
        List<SysSource> result = sysRoleSourceService.getSourceListByUserId(restRequest.getBody().getUserId());
        return new RestResponse().success(result);
    }


}
