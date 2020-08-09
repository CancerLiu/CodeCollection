package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

/**
 * 自定义异常类
 */
public class CustomException extends RuntimeException {

    /**
     * 自定义序列化号
     */
    private final static long serialVersionUID = 112233L;


    public CustomException() {
    }

    /**
     * @param message 异常原因，后同
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * @param message 异常原因，后同
     * @param cause 底层异常，可以借此简历异常链条。
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
