package com.qidian.mall.user.response;

import com.qidian.mall.user.entity.SysSource;
import lombok.Data;

import java.util.List;

/**
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class SysSourceTreeVo extends SysSource {
    private static final long serialVersionUID = -9140386303611423408L;

    private List<SysSourceTreeVo> children;

    /**
     * 是否拥有子节点
     */
    private Boolean hasChildren;
}
