package com.openvector.modelcore.exception;

/**
 * 具体异常子类
 *
 * @author cxc
 */
public class DataSourceNotFoundException extends DataProcessingException{

    public DataSourceNotFoundException(String message) {
        super(message);
    }
}
