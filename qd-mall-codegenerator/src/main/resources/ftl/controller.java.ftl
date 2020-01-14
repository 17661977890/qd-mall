package ${package.Controller};

import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.central.base.mvc.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.request.${entity}DTO;
import com.qidian.mall.response.${entity}VO;
import ${package.Service}.${table.serviceName};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
<#if restControllerStyle>
@Api(tags = {"${table.comment!}web层"})
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} extends BaseController {
</#if>

    @Autowired
    public ${table.serviceName} ${table.entityPath}Service;

    /**
    * 新增${table.comment!}数据
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "保存", notes = "保存数据到${entity}")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse add${entity}(@RequestBody RestRequest<${entity}DTO> restRequest) {
        //BaseController国际化的使用
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        Integer result = ${table.entityPath}Service.save(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 更新${table.comment!}(根据主键id更新)
    * @param restRequest 修改参数
    * @return 是否更改成功
    */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新${entity}数据")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse update${entity}ById(@RequestBody RestRequest<${entity}DTO> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        Integer result = ${table.entityPath}Service.updateById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 删除${table.comment!}(根据主键id伪删除)
    * @param restRequest 主键id
    * @return 是否删除成功
    */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除${entity}数据")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse delete${entity}ById(@RequestBody RestRequest<String> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        Integer result = ${table.entityPath}Service.deleteById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 根据主键id查询单条${table.comment!}
    * @param restRequest 主键id
    * @return 查询结果
    */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取${entity}数据")
    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse get${entity}ById(@RequestBody RestRequest<String> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        ${entity}VO result = ${table.entityPath}Service.selectById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 查询全部${table.comment!}
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "全部查询", notes = "查询${entity}全部数据")
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse get${entity}All(@RequestBody RestRequest<${entity}DTO> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        List<${entity}VO> result = ${table.entityPath}Service.selectAll(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 分页查询${table.comment!}
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "分页查询", notes = "分页查询${entity}全部数据")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse get${entity}Page(@RequestBody RestRequest<${entity}DTO> restRequest) {
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        IPage<${entity}VO> result = ${table.entityPath}Service.selectPage(restRequest.getBody());
        return new RestResponse().success(result);
    }

}
</#if>
