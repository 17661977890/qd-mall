package ${package.Service};

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Entity}.${entity};
import com.qidian.mall.request.${entity}DTO;
import com.qidian.mall.response.${entity}VO;
import java.util.List;
import ${superServiceClassPackage};

/**
* <p>
 * ${entity}服务类
 * </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
     /**
     * 保存信息对象
     * @param record 信息对象
     * @return 影响记录数
     */
     Integer save(${entity}DTO record);

     /**
     * 根据主键更新信息对象
     * @param record 信息对象
     * @return 影响记录数
     */
     Integer updateById(${entity}DTO record);

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
     ${entity}VO selectById(String id);

    /**
     * 根据条件查询信息对象
     * @param record 查询请求条件
     * @return 信息列表
     */
     List<${entity}VO> selectAll(${entity}DTO record);

      /**
      * 分页查询信息对象
      * @param record 查询请求条件
      * @return 信息列表
      */
      IPage<${entity}VO> selectPage(${entity}DTO record);
}
</#if>