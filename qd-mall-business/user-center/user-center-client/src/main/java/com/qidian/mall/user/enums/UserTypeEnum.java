package com.qidian.mall.user.enums;

/**
 * @Author binsun
 * @Date 2020-09-21
 * @Description
 */
public enum UserTypeEnum {
    //处理状态
    APP("APP", "app用户"),
    MERCHANT("MERCHANT", "商家用户"),


    ;


    private String code;
    private String desc;

    UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code){
        if(code == null){
            return null;
        }
        for(UserTypeEnum typeEnum : UserTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
