package com.qidian.mall.message.enums;

/**
 * 短信模版枚举枚举
 *
 * @author: dcliu
 * @date: 2019/8/21 14:42
 */
public enum SmsTemplateTypeEnum {

    WEB(1, "SMS_199773002","注册"),
    MOBILE(2,"SMS_199773002", "登录"),
    ABILITY(3, "SMS_199773002","重置密码");

    private Integer code;
    private String templateCode;
    private String desc;

    SmsTemplateTypeEnum(Integer code, String templateCode,String desc) {
        this.code = code;
        this.desc = desc;
        this.templateCode=templateCode;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTemplateCode() {
        return templateCode;
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
        for(SmsTemplateTypeEnum typeEnum : SmsTemplateTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    public static String getDescByCode(Integer code){
        if(code == null){
            return null;
        }
        for(SmsTemplateTypeEnum typeEnum : SmsTemplateTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;
    }

    public static String getTemplateCodeByCode(Integer code){
        if(code == null){
            return null;
        }
        for(SmsTemplateTypeEnum typeEnum : SmsTemplateTypeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getTemplateCode();
            }
        }
        return null;
    }


}
