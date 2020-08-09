package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

import java.lang.annotation.*;

/**
 * 主要用于demo @Inherited和@Repeatable两个注解
 * 可以在类、成员变量和方法上注释
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(value=CustomAnnotationTestTwos.class)
public @interface CustomAnnotationTestTwo {

     Class<? extends CustomClass> customLogic() default CustomClass.class;
}
