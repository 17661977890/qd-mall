package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import com.qidian.mall.request.${entity}DTO;
import com.qidian.mall.response.${entity}VO;
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;

/**
* <p>
 * ${entity}服务实现类
 * </p>
*
* @author ${author}
* @since ${date}
*/
@Slf4j
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {
}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    //国际化依赖注入
    @Autowired
    private MessageSourceService e;

    /**
    * 保存信息对象
    * @param record
    * @return
    */
    @Override
    public Integer save(${entity}DTO record) {
        ${entity} data = this.convertEntity(record);
        data.setCreateTime(new Date());
        return baseMapper.insert(data);
    }

    /**
    * 根据主键更新信息对象
    * @param record 信息对象
    * @return 影响记录数
    */
    @Override
    public Integer updateById(${entity}DTO record) {
        ${entity} data = this.convertEntity(record);
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
    public ${entity}VO selectById(String id) {
        ${entity} data = baseMapper.selectById(id);
        ${entity}VO vo = new ${entity}VO();
        BeanUtils.copyProperties(data,vo);
        return vo;
    }

    /**
    * 根据条件查询信息对象
    * @param record 查询请求条件
    * @return 信息列表
    */
    @Override
    public List<${entity}VO> selectAll(${entity}DTO record) {
        ${entity} data = this.convertEntity(record);
        QueryWrapper<${entity}> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(data);
        List<${entity}> list = baseMapper.selectList(queryWrapper);
        return this.convert(list);
    }

    /**
    * 分页查询
    * @param record 查询请求条件
    * @return 信息列表
    */
    @Override
    public IPage<${entity}VO> selectPage(${entity}DTO record) {
        // DTO -> entity 转换
        ${entity} data = this.convertEntity(record);
        // 分页数据设置
        Page<${entity}> page = new Page<>(record.getCurrent(),record.getSize());
        // 查询条件
        QueryWrapper<${entity}> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(data);
        IPage<${entity}> list = baseMapper.selectPage(page, queryWrapper);
        IPage<${entity}VO> iPage = new Page<>();
        iPage.setRecords(this.convert(list.getRecords()));
        iPage.setCurrent(list.getCurrent());
        iPage.setSize(list.getSize());
        iPage.setTotal(list.getTotal());
        iPage.setPages(list.getPages());
        return iPage;
    }



     /**
     * DTO -> Entity （copy 属性名和类型一致才可以转换）
     * @param dto 对象
     * @return Entity
     */
     private ${entity} convertEntity(${entity}DTO dto){
         ${entity} data = new ${entity}();
<#--         <#list table.fields as field>-->
<#--           <#if field.propertyType.equals("Date")>-->
<#--           data.set${field.capitalName}(DateUtil.parseDateNewFormat(dto.get${field.capitalName}()));-->
<#--           <#else>-->
<#--         data.set${field.capitalName}(dto.get${field.capitalName}());-->
<#--           </#if>-->
<#--         </#list>-->
         BeanUtils.copyProperties(dto, data);
         return data;
     }

     /**
     * 批量：Entity -> VO （copy 属性名和类型一致才可以转换）
     * @param list 对象
     * @return VO对象
     */
     private List<${entity}VO> convert(List<${entity}> list){
         List<${entity}VO> ${table.entityPath}List = new ArrayList<>();
         if (CollectionUtils.isEmpty(list)) {
             return ${table.entityPath}List;
         }
         for (${entity} source : list) {
             ${entity}VO target = new ${entity}VO();
             BeanUtils.copyProperties(source, target);
             ${table.entityPath}List.add(target);
         }
         return ${table.entityPath}List;
     }

}
</#if>