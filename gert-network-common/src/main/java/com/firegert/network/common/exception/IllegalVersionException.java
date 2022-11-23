package com.firegert.network.common.exception;

public class IllegalVersionException extends RuntimeException {

    public IllegalVersionException() {
    }

    public IllegalVersionException(String message) {
        super(message);
    }

    public IllegalVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalVersionException(Throwable cause) {
        super(cause);
    }

    public IllegalVersionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
