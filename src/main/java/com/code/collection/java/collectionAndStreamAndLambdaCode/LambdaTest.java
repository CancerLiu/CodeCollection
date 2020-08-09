package com.code.collection.java.collectionAndStreamAndLambdaCode;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Lambda表达式相关的demo
 * <p>
 * Lambda表达式在编译的时候实际上将会被当成一个“任意类型”的对象，到底需要当成何种类型的对象，这取决于运行环境的需要。
 */
public class LambdaTest {

    private void lambdaFuction() {
        //逻辑
        Function function = (a) -> {
            int b = (Integer) a;
            if (b == 1) {
                return b * 2;
            } else {
                return b * 4;
            }
        };

        //参数
        int a = 2;
        System.out.println(function.apply(a));
    }

    private void lambdaConsumer() {
        //逻辑
        Consumer consumer = (u) -> {
            if (u instanceof User) {
                ((User) u).setAge(((User) u).getAge() + 1);
            } else {
                System.out.println("传入数据有误");
            }
        };

        //参数
        User user = new LambdaTest().new User();
        user.setAge(11);
        consumer.accept(user);
        System.out.println("consumer之后，年龄为:" + user.getAge());
    }

    private void lambdaPredicate() {
        //逻辑
        Predicate predicate = (a) -> {
            if (a instanceof Number) {
                return true;
            } else {
                return false;
            }
        };

        //参数
        int a = 4;
        System.out.println(predicate.test(a));
    }

    public void lambdaSupplier(){
        //逻辑，无输入值，但是会按照内在的逻辑自己生成并返回值
        Supplier supplier = ()-> new Random().nextInt();

        System.out.println(supplier.get());
    }

    class User {
        private int age;
        private boolean sex;
        private double height;

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public boolean isSex() {
            return sex;
        }

        public User setSex(boolean sex) {
            this.sex = sex;
            return this;
        }

        public double getHeight() {
            return height;
        }

        public User setHeight(double height) {
            this.height = height;
            return this;
        }
    }

    public static void main(String[] args) {
        LambdaTest test = new LambdaTest();
        test.lambdaFuction();
        test.lambdaPredicate();
        test.lambdaConsumer();
        test.lambdaSupplier();
    }
}
