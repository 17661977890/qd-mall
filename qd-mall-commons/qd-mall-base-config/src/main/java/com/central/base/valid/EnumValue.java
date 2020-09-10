package com.central.base.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *
 * @ClassName EnumValue
 * @Description 校验枚举值有效性
 * enumClass 枚举类
 * enumMethod 枚举校验方法
 * @author hlli5
 * @Date 2018/11/5 9:51
 * @Version 1.0
 **/
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    /**
     * 必须属性，注解验证失败时的输出消息
     * @return
     */
    String message() default "未定义的值";

    /**
     * 必须属性，注解在验证时所属的组别，用于分组校验
     * @return
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类
     * @return
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 枚举类的值校验方法
     * @return
     */
    String enumMethod();
}
