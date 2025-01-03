package com.openvector.modelcore.exception;

/**
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
