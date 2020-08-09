package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

    private final static Logger logger = LoggerFactory.getLogger(User.class);

    private int age;
    private String name;
    private double height;
    /**
     * 男——true;女————false
     */
    private boolean sex;

    public User() {
    }

    public User(int age, String name, double height, boolean sex) {
        this.age = age;
        this.name = name;
        this.height = height;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public User setHeight(double height) {
        this.height = height;
        return this;
    }

    public boolean isSex() {
        return sex;
    }

    public User setSex(boolean sex) {
        this.sex = sex;
        return this;
    }

    @CustomAnnotationTestOne(key = "haha", mySwitch = true, argCount = 2)
    public void reflectDemoMethodOne(int argOne, long argTwo, String argThree, User argFour) {
        if (argFour.getAge() > this.age) {
            logger.info("传入的值为————argOne:{},argTwo:{},argThree:{}", argOne, argTwo, argThree);
        } else {
            logger.info("当前对象的值为————age:{},sex:{},name:{},height:{}", this.age, this.sex, this.name, this.height);
        }
    }

    @CustomAnnotationTestTwos(value = {@CustomAnnotationTestTwo(customLogic = CustomClassOne.class)
            , @CustomAnnotationTestTwo(customLogic = CustomClassTwo.class)
    })
    public void reflectDemoMethodTwo() {
        logger.info("注解的选择啊，就是你了");
    }

    /**
     * 这里必须用static 静态内部类 这样再解析注解调用其中方法的时候才能直接通过类调用其中方法。
     * 否则需要通过外部类得到内部类，太麻烦了。
     */
    public static class CustomClassOne extends CustomClass {

        public CustomClassOne() {
        }

        @Override
        public void handler() {
            System.out.println("是注解one中打出的哦");
        }
    }

   public static class CustomClassTwo extends CustomClass {
        public CustomClassTwo() {
        }

        @Override
        public void handler() {
            System.out.println("是注解two中打出的哦");
        }
    }
}
