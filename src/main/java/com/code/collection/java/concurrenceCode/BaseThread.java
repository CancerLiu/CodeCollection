package com.code.collection.java.concurrenceCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BaseThread {

    private static final Logger logger = LoggerFactory.getLogger(BaseThread.class);

    private class ThreadExampleOne extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                logger.info(Thread.currentThread().getName() + " " + i);
            }
        }
    }

    private class ThreadExampleTwo implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                logger.info(Thread.currentThread().getName() + " " + i);
            }
        }
    }

    private class ThreadExampleThree implements Callable {
        @Override
        public Object call() {
            //此处模拟有返回值的线程执行体
            return (Thread.currentThread().getName() + " " + new Random().nextInt(10));
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadExampleOne threadExampleOne = new BaseThread().new ThreadExampleOne();

        Thread threadExampleTwo = new Thread(new BaseThread().new ThreadExampleTwo());

        FutureTask<String> task = new FutureTask<String>(new BaseThread().new ThreadExampleThree());
        Thread threadExampleThree = new Thread(task);

        for (int i = 0; i < 10; i++) {
            logger.info(Thread.currentThread().getName() + " " + i);
        }

        threadExampleOne.start();

        threadExampleTwo.start();

        threadExampleThree.start();
        logger.info(task.get());

    }
}
