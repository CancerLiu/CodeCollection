package com.code.collection.java.concurrenceCode;

/**
 * 用于模拟线程之间的相互通信控制
 *
 */
public class ThreadCommunicationTest {


    private static volatile int count;

    private void changeCount() {
        count = count + 1;
    }

    public static void main(String[] args) {
        ThreadCommunicationTest test = new ThreadCommunicationTest();
        new Thread(() -> {
            synchronized (test) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("第一个线程序号:" + i);
                    if (i == 50) {
                        try {
                            test.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        //第二个线程notify后，第一个线程仍然不能马上获得锁，因为第二个线程还拿着呢。需要等第二个线程跑完
        new Thread(() -> {
            synchronized (test) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("第二个线程序号:" + i);
                    if (i == 50) {
                        test.notify();
                    }
                }
            }
        }).start();
    }
}
