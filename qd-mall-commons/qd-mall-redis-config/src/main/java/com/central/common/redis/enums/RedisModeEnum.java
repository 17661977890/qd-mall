package com.central.common.redis.enums;

/**
 * redis 部署模式
 */
public enum RedisModeEnum {

    SINGLE("single","单机模式"),
    CLUSTER("cluster","集群模式"),
    SENTINEL("sentinel","哨兵模式"),

    ;

    private String code;
    private String desc;

    RedisModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 更根据code 获取desc 描述
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        if(code==null){
            return null;
        }
        for (RedisModeEnum typeEnum: RedisModeEnum.values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;

    }
}
