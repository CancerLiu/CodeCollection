package com.code.collection.java.concurrenceCode;

public class JoinThreadTest {

    private class JoinThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                if (i % 100 == 0) {
                    System.out.println("join线程当前的序号为:" + i);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println("主线程当前的序号为:" + i);
            if (i == 50) {
                JoinThread thread = new JoinThreadTest().new JoinThread();
                //先start然后再join
                thread.start();
                thread.join();
            }
        }
    }
}
