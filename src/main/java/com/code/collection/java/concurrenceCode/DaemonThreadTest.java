package com.code.collection.java.concurrenceCode;

public class DaemonThreadTest extends Thread {


    private class DaemonThread extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("守护线程");
            }
        }
    }

    @Override
    public void run() {
        DaemonThread thread = new DaemonThreadTest().new DaemonThread();
        //先设置为守护线程再启动
        thread.setDaemon(true);
        thread.start();

        for (int i = 0; i < 100000; i++) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        DaemonThreadTest daemonThreadTest = new DaemonThreadTest();
        daemonThreadTest.start();
    }
}


