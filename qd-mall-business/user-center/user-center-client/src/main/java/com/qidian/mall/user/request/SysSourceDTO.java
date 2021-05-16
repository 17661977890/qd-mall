package com.qidian.mall.user.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.central.base.valid.EnumValue;
import com.qidian.mall.user.enums.SourceTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 系统资源入参实体
 * @Author binsun
 * @Date
 * @Description
 */
@Data
public class SysSourceDTO implements Serializable {
    private static final long serialVersionUID = -1357154862847898891L;

    public interface Add{}
    public interface Update{}
    public interface Id{}
    public interface Show{}


    /**
     * 资源id
     */
    @NotNull(message = "资源id不能为空",groups = {Update.class, Id.class,Show.class})
    private Long id;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空",groups = {Add.class,Update.class})
    private String sourceName;

    @NotBlank(message = "资源code不能为空",groups = {Add.class,Update.class})
    private String sourceCode;

    /**
     * 上级资源id
     */
    private Long parentId;

    /**
     * 上级资源名称
     */
    private String parentName;

    /**
     * 资源类型 1:目录 2:菜单 3:按钮
     */
    @EnumValue(enumClass = SourceTypeEnum.class, enumMethod = "enumValueValid", message = "资源类型错误（目录：1，菜单：2，按钮：3）",groups = {Update.class, Add.class})
    @NotNull(message = "资源类型不能为空",groups = {Update.class, Add.class})
    private Integer sourceType;

    /**
     * 资源路径url
     */
    @NotBlank(message = "资源路径url不能为空",groups = {Add.class,Update.class})
    private String url;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空",groups = {Add.class,Update.class})
    private Integer sort;

    /**
     * 是否显示 显示：Y、隐藏：N
     */
    @Pattern(regexp = "(^Y$|^N$)",message = "显示标识只能是Y或者N",groups = {Add.class,Update.class,Show.class})
    @NotBlank(message = "显示标识不能为空",groups = {Add.class,Update.class,Show.class})
    private String showFlag;
}
