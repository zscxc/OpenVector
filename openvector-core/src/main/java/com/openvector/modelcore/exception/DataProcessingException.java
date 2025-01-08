package com.openvector.modelcore.exception;

/**
 * 数据处理通用异常
 *
 * @author cxc
 */
public class DataProcessingException extends Exception {

    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
