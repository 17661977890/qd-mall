package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

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
@Api(tags = {"${table.comment!}"})
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
 private ${table.serviceName} targetService;

 /**
 * 查询分页数据
 */
 @ApiOperation(value = "查询分页数据")
 @RequestMapping(value = "/list")
 public ResponseWeb<Page> findListByPage(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,@RequestParam(name = "pageSize", defaultValue = "20") int pageSize){
  return null;
  }


  /**
  * 根据id查询
  */
  @ApiOperation(value = "根据id查询数据")
  @RequestMapping(value = "/getById")
  public ResponseWeb<${entity}> getById(@RequestParam("pkid") String pkid){
   return null;
  }

  /**
  * 新增
  */
  @ApiOperation(value = "新增数据")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public ResponseWeb<${entity}> add(@RequestBody ${entity} ${entity}){
   return null;
  }

  /**
  * 删除
  */
  @ApiOperation(value = "删除数据")
  @RequestMapping(value = "/del")
  public ResponseWeb<String> delete(@RequestParam("ids") List<String> ids){
    return null;
  }

    /**
    * 修改
    */
    @ApiOperation(value = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseWeb<${entity}> update(@RequestBody ${entity} ${entity}){
     return null;
    }

}
</#if>
