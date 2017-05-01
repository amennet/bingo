package com.framework.common.exception;

/**
 * Created by ZhangGe on 2017/4/27.
 */
public class BingoException extends RuntimeException {
    public BingoException(String message) {
        super(message);
    }

    public BingoException(String message, Throwable cause) {
        super(message, cause);
    }
}
