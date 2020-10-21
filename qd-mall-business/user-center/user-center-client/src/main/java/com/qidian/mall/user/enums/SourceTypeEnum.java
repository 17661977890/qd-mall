package com.qidian.mall.user.enums;

/**
 * 资源类型枚举
 * @Author binsun
 * @Date 2020-09-21
 * @Description
 */
public enum SourceTypeEnum {
    //处理状态
    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),


    ;


    private Integer code;
    private String desc;

    SourceTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code){
        if(code == null){
            return null;
        }
        for(SourceTypeEnum typeEnum : SourceTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;
    }

    /**
     * 用于校验枚举值是否存在，允许null和""通过
     * @author
     * @param code
     * @return
     */
    public static boolean enumValueValid(Integer code){
        if(code == null){
            return false;
        }
        for(SourceTypeEnum typeEnum : SourceTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

}
