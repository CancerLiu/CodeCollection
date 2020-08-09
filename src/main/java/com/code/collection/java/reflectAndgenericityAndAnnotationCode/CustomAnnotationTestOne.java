package com.code.collection.java.reflectAndgenericityAndAnnotationCode;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模拟自定义注解，尽量把所有的可能性囊括
 * <p>
 * 自定义注解时的成员变量类型是有限制的，具体如下：
 * （1）基本数据类型(boolean, byte, char, short, int, long, float, double等);
 * （2） String；
 * （3）Class；
 * （4） 枚举；
 * （5）其他的注解；
 * （6）以上类型的数组；
 *
 *  成员变量模式是public修饰符
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotationTestOne {

      String key() default "";

      boolean mySwitch() default true;

     /**
      * 使用方法的第几个参数，为0则不使用
      */
      int argCount() default 0;
}
