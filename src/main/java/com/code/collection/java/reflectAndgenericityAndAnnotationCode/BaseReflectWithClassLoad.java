package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

/**
 * demo 三种反射方式会触发的类加载方式
 */
public class BaseReflectWithClassLoad {


    public static void main(String[] args) throws ClassNotFoundException {
        /**
         * 不会打印任何东西，说明此反射不会进入初始化
         */
//        Class classOne = ClassLoadDemo.class;

        /**
         * 只会初始化，即将类信息加入内存，其不会进行进一步的实例化。所以只有静态代码块中的内容
         */
//        Class classThree = Class.forName("com.code.collection.java.reflectAndgenericityAndAnnotationCode.ClassLoadDemo");

        /**
         * 初始化后会进一步进行实例化(毕竟也是通过类实例得到的嘛，所以肯定会有实例化的啦)。所以不止是静态代码块的内容，普通代码块和
         * 构造器中的内容都被打印出来了。
         */
          Class classTwo = new ClassLoadDemo().getClass();
    }

}
