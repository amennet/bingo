package com.framework.gateway.exception;

/**
 * Created by ZhangGe on 2017/4/4.
 */
public class GatewayException extends Exception {
    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }


}
