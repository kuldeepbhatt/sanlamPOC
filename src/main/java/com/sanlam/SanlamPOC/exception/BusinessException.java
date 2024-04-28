package com.sanlam.SanlamPOC.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String msg, Exception e) {
        super(msg, e);
    }
}