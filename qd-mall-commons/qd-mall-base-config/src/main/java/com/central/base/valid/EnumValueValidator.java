package com.central.base.valid;

import com.central.base.exception.BusinessException;
import com.central.base.util.ConstantUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @ClassName EnumValueValidator
 * @Description 校验枚举值有效性的Validator
 * @author hlli5
 * @Date 2018/11/5 9:53
 * @Version 1.0
 **/
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    private Class<? extends Enum<?>> enumClass;
    private String enumMethod;

    @Override
    public void initialize(EnumValue enumValue) {
        enumClass = enumValue.enumClass();
        enumMethod = enumValue.enumMethod();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return Boolean.TRUE;
        }
        if (enumClass == null || enumMethod == null) {
            return Boolean.TRUE;
        }
        Class<?> valueClass = value.getClass();
        try {
            Method method = enumClass.getMethod(enumMethod, valueClass);
            if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                throw new BusinessException(ConstantUtil.ERROR,String.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
            }

            if(!Modifier.isStatic(method.getModifiers())) {
                throw new BusinessException(ConstantUtil.ERROR,String.format("%s method is not static method in the %s class", enumMethod, enumClass));
            }

            Boolean result = (Boolean)method.invoke(null, value);
            return result == null ? false : result;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new BusinessException(ConstantUtil.ERROR,e.getMessage());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new BusinessException(ConstantUtil.ERROR,String.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass));
        }
    }
}
