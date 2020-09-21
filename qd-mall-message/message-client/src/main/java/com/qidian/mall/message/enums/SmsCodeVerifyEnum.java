package com.qidian.mall.message.enums;


/**
 * 消息验证码 校验信息枚举
 * @Author binsun
 * @Date 2020-02-10
 * @Description
 */
public enum SmsCodeVerifyEnum {

    //处理状态
    VERIFY_SUCCESS("success", "校验通过"),
    VERIFY_FAILURE("failure", "校验未通过"),
    NOT_VERIFIED("not_verified", "未校验");

    ;


    private String code;
    private String desc;

    SmsCodeVerifyEnum(String code, String desc) {
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
        for(SmsCodeVerifyEnum typeEnum : SmsCodeVerifyEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
