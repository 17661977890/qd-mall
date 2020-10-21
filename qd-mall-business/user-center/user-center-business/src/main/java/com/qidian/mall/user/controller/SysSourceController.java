package com.qidian.mall.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.user.entity.SysSource;
import com.qidian.mall.user.request.SysSourceDTO;
import com.qidian.mall.user.response.SysSourceVo;
import com.qidian.mall.user.service.ISysSourceService;
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
 * 系统资源 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Slf4j
@Api(tags = {"资源管理"})
@RestController
@RequestMapping("/sys-source")
public class SysSourceController extends BaseController {

    @Autowired
    public ISysSourceService sysSourceService;

    
    /**
    * 新增系统资源数据
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "新增", notes = "新增数据到SysSource")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse addSysSource(@Validated(SysSourceDTO.Add.class) @RequestBody RestRequest<SysSourceDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal(); 
        Integer result = sysSourceService.addSource(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }

    /**
    * 更新系统资源(根据主键id更新)
    * @param restRequest 修改参数
    * @return 是否更改成功
    */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新SysSource数据")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse updateSysSourceById(@Validated(SysSourceDTO.Update.class) @RequestBody RestRequest<SysSourceDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Integer result = sysSourceService.updateSource(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }

    /**
    * 删除系统资源(根据主键id伪删除)
    * @param restRequest 主键id
    * @return 是否删除成功
    */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除SysSource数据")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse deleteSysSourceById(@Validated(SysSourceDTO.Id.class) @RequestBody RestRequest<SysSourceDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Integer result = sysSourceService.deleteById(restRequest.getBody().getId(),username);
        return new RestResponse().success(result);
    }

    /**
    * 根据主键id查询单条系统资源
    * @param restRequest 主键id
    * @return 查询结果
    */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取SysSource数据")
    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysSourceById(@Validated(SysSourceDTO.Id.class)  @RequestBody RestRequest<SysSourceDTO> restRequest) {
        SysSourceVo result = sysSourceService.selectById(restRequest.getBody().getId());
        return new RestResponse().success(result);
    }

    /**
    * 查询全部系统资源
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "树结构资源查询", notes = "查询SysSource树")
    @RequestMapping(value = "/getTreeList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getTreeList(@RequestBody RestRequest<SysSourceDTO> restRequest){
        List<Object> result = sysSourceService.getTreeList(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
     * 查询全部系统资源
     * @param restRequest 查询条件
     * @return 查询结果
     */
    @ApiOperation(value = "全部查询", notes = "条件查询SysSource全部数据")
    @RequestMapping(value = "/getListBy", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getListBy(@RequestBody RestRequest<SysSourceDTO> restRequest)  {
        List<SysSourceVo> result = sysSourceService.getListBy(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 分页查询系统资源
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "分页查询", notes = "分页查询SysSource全部数据")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysSourcePage(@RequestBody RestRequest<SysSourceDTO> restRequest) {
        if(restRequest.getHeader().getPageNum()==null || restRequest.getHeader().getPageSize()==null){
            throw new BusinessException("102102",getMessage("102102"));
        }
        IPage<SysSourceVo> result = sysSourceService.selectPage(restRequest.getBody(),restRequest.getHeader().getPageSize(),restRequest.getHeader().getPageNum());
        return new RestResponse().success(result);
    }

    /**
     * 更新系统资源(根据主键id更新)
     * @param restRequest 修改参数
     * @return 是否更改成功
     */
    @ApiOperation(value = "修改显示状态", notes = "根据主键id更新SysSource数据")
    @RequestMapping(value = "/updateShowStatus", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse updateShowStatus(@Validated(SysSourceDTO.Show.class) @RequestBody RestRequest<SysSourceDTO> restRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Integer result = sysSourceService.updateShowStatus(restRequest.getBody(),username);
        return new RestResponse().success(result);
    }

}
