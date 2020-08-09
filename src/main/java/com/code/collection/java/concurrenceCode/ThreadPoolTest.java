package com.code.collection.java.concurrenceCode;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 线程池相关的demo
 */
public class ThreadPoolTest {

    private BaseThread thread;

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    private class ThreadExampleOne extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                logger.info("normal Thread" + " " + i);
            }
        }
    }

    private class ThreadExampleTwo implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                logger.info("runnable Thread" + " " + i);
            }
        }
    }

    private class ThreadExampleThree implements Callable {
        @Override
        public Object call() {
            //此处模拟有返回值的线程执行体
            return ("Callable Thread" + " " + new Random().nextInt(10));
        }
    }

    /**
     * 创建自定义线程池
     *
     * @param corePoolSize  线程池维护线程的最少数量(也是核心线程的数量)
     * @param maxPoolSize   线程池维护线程的最大数量
     * @param keepAliveTime 线程池维护线程所允许的空闲时间
     */
    private ExecutorService customCreateThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime) {
        //这里使用google的guava来提供线程工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("\"Orders-%d\"").build();

        return new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MICROSECONDS/*毫秒*/,
                new LinkedBlockingQueue<Runnable>()/*等待队列*/, threadFactory/*生产线程的工厂*/);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int threadNum = 4;

        //固定线程数量的，类似于newSingleThreadPool
        ExecutorService services = Executors.newFixedThreadPool(threadNum);

        //模拟普通池子
        services.execute(new ThreadPoolTest().new ThreadExampleOne());

        //模拟Runnable的程序
        Future future1 = services.submit(new ThreadPoolTest().new ThreadExampleTwo());
        System.out.println(future1.get());

        //模拟Callable的程序
        Future future2 = services.submit(new ThreadPoolTest().new ThreadExampleThree());
        while (!future2.isDone()) {
            if (null == future2.get()) {
                System.out.println("暂时没返回");
            } else {
                System.out.println("返回的内容为:" + future2.get());
            }
        }


        //自定义线程池
        System.out.println("自定义线程池内容:");
        ThreadPoolTest poolTest = new ThreadPoolTest();
        ExecutorService threadPool = poolTest.customCreateThreadPool(5, 10, 5000);
        threadPool.execute(new ThreadPoolTest().new ThreadExampleOne());
    }
}
