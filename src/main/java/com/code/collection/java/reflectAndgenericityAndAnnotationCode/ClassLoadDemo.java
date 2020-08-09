package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 为了测试三种反射方式走的类加载路径所写的demo类
 */
public class ClassLoadDemo {

    private final static Logger logger = LoggerFactory.getLogger(ClassLoadDemo.class);

    static {
        logger.info("静态代码块中的内容1");
    }

    {
        logger.info("普通代码块中的内容2");
    }

    public ClassLoadDemo() {
        logger.info("构造器中的内容3");
    }
}
