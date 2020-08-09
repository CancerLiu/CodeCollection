package com.code.collection.java.concurrenceCode;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 一个jdk8新出的forkJoinPool线程池的demo
 * <p>
 * 使用并行池打印大数字并和不使用并行时的时间进行对比
 */
public class ForkJoinPoolTest {

    private final static Logger logger = LoggerFactory.getLogger(ForkJoinPoolTest.class);

    /**
     * 需要打印的数字总数量
     */
    private int toPrint = 500000;

    private  long normalTime;
    private  long forkJoinPoolTime;

    /**
     * 用来模拟逻辑处理
     */
    private Random random = new Random(1000);

    private void normalPrint() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < toPrint; i++) {
            random.nextInt();
        }
         normalTime = System.currentTimeMillis() - startTime;
        logger.info("普通打印用时为:{}", normalTime);
    }

    class PrintTask extends RecursiveAction {

        private static final int THRESHOLD = 50;
        private int start;
        private int end;

        public PrintTask(int start, int end) {
            super();
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if ((end - start) < THRESHOLD) {
                for (int i = start; i < end; i++) {
                    random.nextInt();
                }
            } else {
                int middle = (end + start) / 2;
                PrintTask left = new PrintTask(start, middle);
                PrintTask right = new PrintTask(middle, end);

                //继续分批执行两个小任务
                left.fork();
                right.fork();
            }
        }
    }

    private void forkJoinPrint() {
        //jdk8 新加入的通用方法，得到一个通用的forkJoinPool池对象
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(new PrintTask(0, toPrint));
         forkJoinPoolTime = System.currentTimeMillis() - startTime;
        logger.info("ForkJoinPool打印用时为:{}", forkJoinPoolTime);
        forkJoinPool.shutdown();
    }

    public static void main(String[] args) {
        ForkJoinPoolTest test = new ForkJoinPoolTest();
        test.normalPrint();
        test.forkJoinPrint();


        logger.info("最终普通打印所需时间为{},forkJoin池打印所需时间为{}",test.normalTime,test.forkJoinPoolTime);
    }


}
