package com.framework.register.support;

/**
 * Wrapper异常，用于指示 {@link FailbackRegistry}跳过Failback。
 */
public class SkipFailbackWrapperException extends RuntimeException {
    public SkipFailbackWrapperException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // do nothing
        return null;
    }
}
