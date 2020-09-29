package com.qidian.mall.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.request.SysRoleDTO;
import com.qidian.mall.user.response.SysRoleVo;
import com.qidian.mall.user.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 系统角色 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Slf4j
@Api(tags = {"角色管理"})
@RestController
@RequestMapping("/sys-role")
public class SysRoleController extends BaseController {

    @Autowired
    public ISysRoleService sysRoleService;

    
    /**
    * 新增系统角色数据
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "新增", notes = "新增数据到SysRole")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse addSysRole(@Validated(SysRoleDTO.Add.class) @RequestBody RestRequest<SysRoleDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal(); 
        Integer result = sysRoleService.addRole(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }

    /**
    * 更新系统角色(根据主键id更新)
    * @param restRequest 修改参数
    * @return 是否更改成功
    */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新SysRole数据")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse updateSysRoleById(@Validated(SysRoleDTO.Update.class) @RequestBody RestRequest<SysRoleDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Integer result = sysRoleService.updateRole(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }

    /**
    * 删除系统角色(根据主键id伪删除)
    * @param restRequest 主键id
    * @return 是否删除成功
    */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除SysRole数据")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse deleteSysRoleById(@Validated(SysRoleDTO.Id.class) @RequestBody RestRequest<SysRoleDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Integer result = sysRoleService.deleteById(restRequest.getBody().getId(),username);
        return new RestResponse().success(result);
    }

    /**
    * 根据主键id查询单条系统角色
    * @param restRequest 主键id
    * @return 查询结果
    */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取SysRole数据")
    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysRoleById(@Validated(SysRoleDTO.Id.class)  @RequestBody RestRequest<SysRoleDTO> restRequest) {
        SysRoleVo result = sysRoleService.selectById(restRequest.getBody().getId());
        return new RestResponse().success(result);
    }

    /**
    * 查询全部系统角色
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "全部查询", notes = "查询SysRole全部数据")
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysRoleAll(@RequestBody RestRequest<SysRoleDTO> restRequest) throws InterruptedException {
        List<SysRoleVo> result = sysRoleService.selectAll(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 分页查询系统角色
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "分页查询", notes = "分页查询SysRole全部数据")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysRolePage(@RequestBody RestRequest<SysRoleDTO> restRequest) {
        if(restRequest.getHeader().getPageNum()==null || restRequest.getHeader().getPageSize()==null){
            throw new BusinessException("102102",getMessage("102102"));
        }
        IPage<SysRoleVo> result = sysRoleService.selectPage(restRequest.getBody(),restRequest.getHeader().getPageSize(),restRequest.getHeader().getPageNum());
        return new RestResponse().success(result);
    }


}
