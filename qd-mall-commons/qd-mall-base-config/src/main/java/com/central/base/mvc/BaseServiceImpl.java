package com.central.base.mvc;

import com.central.base.message.MessageSourceService;
import com.central.base.util.ConstantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基础base serviceImpl
 * @Author binsun
 * @Date 2020-01-13
 * @Description
 */
public class BaseServiceImpl<T> {

    // ================================== 国际化相关 ==================================
    @Autowired
    private MessageSourceService messageSourceService;

    protected String getMessage(String code){
        return messageSourceService.getMessage(code);
    }

    // ================================== 公共属性填充 =================================

    /**
     * 更新用户公共字段
     * @param bean
     * @param username
     */
    protected void updateCommonField(BaseEntity bean, String username) {
        bean.setUpdateTime(new Date());
        if (username != null) {
            bean.setUpdateUser(username);
        } else {
            bean.setUpdateUser("system");
        }

    }

    /**
     * 封装用户公共字段信息
     * @param bean
     * @param username
     */
    protected void addCommonField(BaseEntity bean, String username) {
        Date now = new Date();
        bean.setDeleteFlag(ConstantUtil.DELETE_FLAG_N);
        bean.setCreateTime(now);
        bean.setUpdateTime(now);
        if (username != null) {
            bean.setCreateUser(username);
            bean.setUpdateUser(username);
        } else {
            bean.setCreateUser("system");
            bean.setUpdateUser("system");
        }
        bean.setVersion(ConstantUtil.INIT_VERSION);
    }

    // ======================================== 对象转换方法 =============================
    /**
     * DTO -> Entity （copy 属性名和类型一致才可以转换）
     * @param dto 对象
     * @return Entity
     */
    private T convertEntity(Class<T> c,Object dto){
        T data = null;
        try {
            data = c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert data != null;
        BeanUtils.copyProperties(dto, data);
        return data;
    }

    /**
     * 批量：Entity -> VO （copy 属性名和类型一致才可以转换）
     * @param list 对象
     * @return VO对象
     */
    private List<Object> convert(List<T> list){
        List<Object> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (T source : list) {
            Object target = new Object();
            BeanUtils.copyProperties(source, target);
            result.add(target);
        }
        return result;
    }
}
