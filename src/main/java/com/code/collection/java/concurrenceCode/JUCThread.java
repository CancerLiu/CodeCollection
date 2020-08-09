package com.code.collection.java.concurrenceCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这里主要用来做JUC常见的三个类的demo
 * <p>
 * 其实主要就是三个类————Semaphore、CountDownLatch与CyclicBarrier
 * <p>
 * 同时注意，这几个JUC下的实现类都需要获取到监视器对象或Lock对象才能运行
 */
public class JUCThread {

    private ThreadPoolExecutor threadPoolExecutor;

    private static final Logger logger = LoggerFactory.getLogger(JUCThread.class);

    private AtomicInteger threadNum = new AtomicInteger(0);


    public JUCThread() {
        //创建一个自定义线程池。这里不适用Executors工具类，而使用自定义来创建线程池
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(20);

        /**
         * 池子中所保存的线程数，包括空闲线程
         */
        int corePoolSize = 5;

        /**
         * 池中允许的最大线程数
         */
        int maximumPoolSize = 50;

        /**
         * 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
         */
        int keepAliveTime = 3;

        //这里使用了默认的线程工厂
//        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MICROSECONDS, blockingQueue);
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(250);
    }


    /**
     * semaphone的demo
     * <p>
     * 因为Semaphore规定的是20，所以最终打印出来的东西应该是20个为一组的
     */
    private void semaphoreTest() {
        //初始化semaphore
        Semaphore semaphore = new Semaphore(20);
        //循环次数(需要比Semaphore大)
        int threadCount = 80;
        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    //得到许可证信号量
                    semaphore.acquire();
                    consumerTest();
                    //还回许可证信号量
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadPoolExecutor.shutdown();
        logger.info("finish");
    }

    /**
     * CountDownLatch是一次性的，计数器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，
     * 当CountDownLatch使用完毕后，它不能再次被使用。
     */
    private void countDownLatch() throws InterruptedException {
        //初始化CountDownLatch对象
        CountDownLatch countDownLatch = new CountDownLatch(20);
        //循环次数(需要大于20，否则执行主线程会一直等待)
        int threadCount = 80;

        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    //先执行
                    consumerTest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //执行完后，当前线程-1
                    countDownLatch.countDown();
                }
            });
        }

        //主线程等待
        countDownLatch.await();
        threadPoolExecutor.shutdown();
        logger.info("finish");
    }

    /**
     * 和CountDownLatch很类似。只是这里是主动选择需要等待的线程
     */
    private void cyclicBarrierTest() {
        //初始化Barrier对象
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        int threadCount = 5;
        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    //模拟等待时间
                    Thread.sleep(5000);
                    consumerTest();
                    //等待其他线程
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        threadPoolExecutor.shutdown();
        if(threadPoolExecutor.isShutdown()){
            logger.info("finish");
        }

    }


    private void consumerTest() throws InterruptedException {
        Thread.sleep(1000);
        logger.info("当前的index为{}", threadNum.getAndIncrement());
        Thread.sleep(1000);
    }


    public static void main(String[] args) throws InterruptedException {
        JUCThread thread = new JUCThread();
//        thread.semaphoreTest();
//        thread.countDownLatch();
         thread.cyclicBarrierTest();
    }
}
