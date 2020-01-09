package ${package.Controller};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import  ${package.Service}.${table.serviceName};

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
public class ${table.controllerName} {
</#if>

      @Autowired
      private ${table.serviceName} ${table.serviceName};

      /**
       * 查询分页数据
       */
      @ApiOperation(value = "查询分页数据")
      @RequestMapping(value = "/list")
      public RestResponse findListByPage(@RequestBody RestRequest<${entity}> restRequest){
       return null;
      }


      /**
       * 根据id查询详情
       */
      @ApiOperation(value = "根据id查询数据")
      @RequestMapping(value = "/getById")
      public RestResponse getById(@RequestParam("id") String id){
       return null;
      }

      /**
       * 新增
       */
      @ApiOperation(value = "新增数据")
      @RequestMapping(value = "/add", method = RequestMethod.POST)
      public RestResponse add(@RequestBody ${entity} ${entity}){
       return null;
      }

      /**
       * 删除
       */
      @ApiOperation(value = "删除数据")
      @RequestMapping(value = "/del")
      public RestResponse delete(@RequestParam("ids") List<String> ids){
        return null;
      }

      /**
       * 修改
       */
      @ApiOperation(value = "更新数据")
      @RequestMapping(value = "/update", method = RequestMethod.POST)
      public RestResponse update(@RequestBody ${entity} ${entity}){
       return null;
      }

}
</#if>
