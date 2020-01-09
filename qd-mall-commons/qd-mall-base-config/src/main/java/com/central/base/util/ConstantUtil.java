package com.central.base.util;

/**
 * 公共常量类定义
 * @Author 彬
 * @Date 2019/4/20
 */
public class ConstantUtil {

    /**
     * 返回状态码：
     */
    public static final String SUCCESS = "200";

    public static final String  ERROR = "500";

    public static final String UN_KNOW_ERROR = "400";

    /**
     * 操作项码：----修改数据状态
     */
    //提交
    public static final String COMMIT = "commit";
    //撤销
    public static final String ABOLISH = "abolish";
    //审核
    public static final String AUDIT = "audit";
    //反审核
    public static final String REVERSE_AUDIT = "reverse_audit";
    //删除
    public static final String DELETE = "delete";
    //禁用
    public static final String FORBIDDEN = "forbidden";
    //反禁用
    public static final String UN_FORBIDDEN = "un_forbidden";


    /**
     * 删除标识
     */
    public static final String YES_OR_NO_N ="N";
    public static final String YES_OR_NO_Y ="Y";
    /**
     * 菜单可用不可见
     */
    public static final String UN_SEE = "0";

    /**
     * 用户类型
     */
    //内部
    public static final String INNER_USER ="INNER_USER";
    //外部
    public static final String OUTER_USER ="OUTER_USER";

    /**
     * 联系对象类型
     */
    //员工
    public static final String EMPLOYEE ="EMPLOYEE";

    /**
     * 角色属性（公有/私有）
     */
    public static final String PUBLIC ="PUBLIC";
    public static final String PRIVATE ="PRIVATE";

    /**
     * 核算组织（法人/利润中心）
     */
    public static final String LEGAL_PERSON ="LEGAL_PERSON";
    public static final String PROFIT_CENTER ="PROFIT_CENTER";
    /**
     * 资源类型（菜单/按钮）
     */
    public static final String MENU ="MENU";
    public static final String BUTTON ="BUTTON";

    /**
     * 权限状态
     */
    public static final String HAS_POWER = "1";
    public static final String HAS_NO_POWER = "0";
    public static final String POWER_FORBIDDEN = "2";

    /**
     * 权限组
     */
    public static final String GROUP_QUERY = "PG1";
    public static final String GROUP_EDIT = "PG2";
    public static final String GROUP_AUDIT = "PG3";
    public static final String GROUP_BUSINESS_OPERATION = "PG4";
    public static final String GROUP_OTHER_ITEM = "PG5";

    /**
     * 性别：1男2女
     */
    public static final String male = "1";
    public static final String female = "2";
}
