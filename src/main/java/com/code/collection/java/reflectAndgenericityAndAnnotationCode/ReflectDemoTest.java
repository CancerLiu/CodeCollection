package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

/**
 * 反射的demo 1，以User类为对象
 */
public class ReflectDemoTest {

    private final static Logger logger = LoggerFactory.getLogger(ReflectDemoTest.class);

    private void baseMethod() throws InvocationTargetException, IllegalAccessException {
        User user = new User(18, "liuchao", 1.78, true);

        //取得class对象
        Class userClass = user.getClass();

        //得到方法对象
        Method[] methods = userClass.getDeclaredMethods();

        Optional<Method> optionlMethod = Arrays.stream(methods).filter(m -> m.getName().equals("getAge")).findFirst();


        if (optionlMethod.isPresent()) {
            Method method = optionlMethod.get();
            //进行调用
            System.out.println(method.invoke(user));
        }
        return;
    }

    private void baseField() throws IllegalAccessException {
        User user = new User(18, "liuchao", 1.78, true);

        //取得class对象
        Class userClass = User.class;

        Field[] fields = userClass.getDeclaredFields();

        Optional<Field> optionalField = Arrays.stream(fields).filter(f -> f.getName().equals("name")).findFirst();

        if (optionalField.isPresent()) {
            Field field = optionalField.get();
            field.setAccessible(true);
            System.out.println(field.get(user));
        }
        return;
    }

    private void methodWithAnnotationOne() {
        User user = new User(18, "liuchao", 1.78, true);

        //取得class对象
        Class userClass = User.class;

        Method[] methods = userClass.getDeclaredMethods();

        Method method = Arrays.stream(methods).filter(m -> m.getAnnotation(CustomAnnotationTestOne.class) != null).findFirst().get();

        CustomAnnotationTestOne annotation = method.getAnnotation(CustomAnnotationTestOne.class);

        if (annotation.mySwitch()) {
            Parameter[] parameters = method.getParameters();
            String paramName = parameters[annotation.argCount()].getName();
            logger.info(annotation.key() + "  " + paramName);
        }
    }

    private void methodWithAnnotationTwo() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        User user = new User(18, "liuchao", 1.78, true);

        //取得class对象
        Class userClass = User.class;

        Method[] methods = userClass.getDeclaredMethods();

        Method method = Arrays.stream(methods).filter(m -> m.getAnnotation(CustomAnnotationTestTwos.class) != null).findFirst().get();

        CustomAnnotationTestTwos annotation = method.getAnnotation(CustomAnnotationTestTwos.class);

        //得到注解容器中的具体注解
        CustomAnnotationTestTwo[] anntations = annotation.value();

        for(CustomAnnotationTestTwo a:anntations){
            //得到具体方法
            (a.customLogic().newInstance()).handler();
        }
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        ReflectDemoTest one = new ReflectDemoTest();
//        one.baseField();
//        one.baseMethod();
//        one.methodWithAnnotationOne();
        one.methodWithAnnotationTwo();
    }

}
