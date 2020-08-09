package com.code.collection.java.concurrenceCode;

import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同时也模拟Condition相关内容。
 * 同时也引入BlockingQueue内容
 */
public class ConditionTest {

    private int queueSize = 10;
    private BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(queueSize);

    /**
     * 可重入锁
     */
    private Lock lock = new ReentrantLock();
    /**
     * 判断非满时所需的Condition
     */
    private Condition notFull = lock.newCondition();
    /**
     * 判断非空时所需的Condition
     */
    private Condition notEmpty = lock.newCondition();

    class Consumer extends Thread {
        @Override
        public void run() {
            consumer();
        }

        volatile boolean flag = true;

        private void consumer() {
            while (flag) {
                lock.lock();
                try {
                    //队列空则让producer生产
                    while (queue.isEmpty()) {
                        try {
                            System.out.println("队列空，等待数据");
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            //报错则不继续循环，后同
                            flag = false;
                        }
                    }
                    queue.poll();
                    //唤醒
                    notFull.signal();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Producer extends Thread {
        @Override
        public void run() {
            producer();
        }

        volatile boolean flag = true;

        private void producer() {
            while (flag) {
                lock.lock();
                try {
                    //队列空则让producer生产
                    while (queue.size() == queueSize) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            notFull.await();
                        } catch (InterruptedException e) {
                            flag = false;
                        }
                    }
                    queue.offer(1);
                    //唤醒
                    notEmpty.signal();
                    System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        /**
         * 这里需要注意下的是，需要是同一个对象的Lock锁。之前写错了，写成了
         *
         * Consumer consumer = new ConditionTest().new Consumer();
         * Producer producer = new ConditionTest().new Producer();
         *
         * 这样导致consumer和producer是不同的锁，从而不能相互通信！！
         */

        ConditionTest conditionTest=new ConditionTest();
        Consumer consumer = conditionTest.new Consumer();
        Producer producer = conditionTest.new Producer();

        consumer.start();
        producer.start();
    }
}
