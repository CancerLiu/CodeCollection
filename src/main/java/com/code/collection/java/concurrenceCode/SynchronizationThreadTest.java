package com.code.collection.java.concurrenceCode;

import java.util.Random;

/**
 * 用于测试对象锁与类锁的相互同步关系
 */
public class SynchronizationThreadTest {

    private static volatile int count;


    /**
     * 这是对象锁默认得到当前类实例对象
     */
    public synchronized void changeCountA() {
        count = count + 1;
        System.out.println("当前的count值为:" + count + "  " + Thread.currentThread().getName());
    }

    /**
     * 指定的this对象锁，应该是和changeCountA方法作用一致
     */
    public void changeCountB() {
        synchronized (this) {
            count = count + 1;
            System.out.println("当前的count值为:" + count + "  " + Thread.currentThread().getName());
        }
    }

    /**
     * 指定一个对象当作对象锁，而不是以当前类的实例对象来当作对象锁
     */
    public void changeCountC() {
        BaseThread thread = new BaseThread();
        synchronized (thread) {
            count = count + 1;
            System.out.println("当前的count值为:" + count + "  " + Thread.currentThread().getName());
        }
    }

    /**
     * 类锁，最基本的形式
     */
    public static synchronized void changeCountD() {
        count = count + 1;
        System.out.println("当前的count值为:" + count + "  " + Thread.currentThread().getName());
    }

    /**
     * 类锁，锁住当前类的所有实例对象
     */
    public void changeCountE() {
        synchronized (SynchronizationThreadTest.class) {
            count = count + 1;
            System.out.println("当前的count值为:" + count + "  " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {

        //只要打印出的count值没有重复就表示锁住了资源
        /*
        * 这是一个关于不同类型锁对于线程同步的测试。下面的//1和//2会调用上面不同的方法，以此来测试对于count是否有同步效果。
        * 可知  changeCountA和changeCountB是有同步效果的，因为其都是以当前实例对象为锁的
        *       changeCountC和changeCountA(或changeCountB)没有同步效果，因为锁不同
        *       changeCountE和changeCountD具有同步锁，都是以当前SynchronizationThreadTest的类为锁
        *       changeCountE(或changeCountD)对changeCountA或changeCountB具有同步锁效果(锁住SynchronizationThreadTest的所有实例对象)。
        *       反之和changeCountC就没有了。
        * */
        SynchronizationThreadTest test = new SynchronizationThreadTest();
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int flagInt = new Random().nextInt(10);
                    if (flagInt % 2 == 0) {
                        //1
                        test.changeCountA();
                    } else {
                        //2
                        test.changeCountE();
                    }
                }
            }).start();
        }
    }
}
