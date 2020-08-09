package com.code.collection.java.concurrenceCode;

public class ThreadLocalTest {

    private static final ThreadLocal<BaseThread> threadLocalDemo = new ThreadLocal<>();

    public void setThreadLocalDemo(BaseThread b) {
        threadLocalDemo.set(b);
    }

    public BaseThread getThreadLocalDemo() {
        return threadLocalDemo.get();
    }

    public void reset() {
        threadLocalDemo.remove();
    }
}
