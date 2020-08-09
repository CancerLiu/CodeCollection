package com.code.collection.java.concurrenceCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class RunnableTest implements Runnable {

    private AtomicInteger count = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(RunnableTest.class);

    @Override
    public void run() {

        logger.info(Thread.currentThread().getName() + "    " +  count.getAndIncrement());
    }

    public static void main(String[] args) {
        RunnableTest test1 = new RunnableTest();
//        RunnableTest test2 = new RunnableTest();
//        RunnableTest test3 = new RunnableTest();
//        RunnableTest test4 = new RunnableTest();
//        RunnableTest test5 = new RunnableTest();

        Thread threadOne = new Thread(test1);
        Thread threadTwo = new Thread(test1);
        Thread threadThree = new Thread(test1);
        Thread threadFour = new Thread(test1);
        Thread threadFive = new Thread(test1);

        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadFour.start();
        threadFive.start();
    }
}
