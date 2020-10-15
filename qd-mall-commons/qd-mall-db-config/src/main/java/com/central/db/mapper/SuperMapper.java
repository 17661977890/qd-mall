package com.central.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mapper 父类，注意这个类不要让 mp 扫描到！！
 * @author mall
 */
public interface SuperMapper<T> extends BaseMapper<T> {
    // 这里可以放一些公共的方法

    /**
     * 自定义通用方法
     */
    Integer deleteAll();

    int myInsertAll(T entity);


    int insertBatchSomeColumn(List<T> list);

    int deleteByIdWithFill(T entity);

    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

}
