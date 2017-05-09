package com.framework.gateway.exception;

/**
 * Created by ZhangGe on 2017/4/4.
 */
public class GatewayRuntimeException extends RuntimeException {
    public GatewayRuntimeException(String message) {
        super(message);
    }

    public GatewayRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
