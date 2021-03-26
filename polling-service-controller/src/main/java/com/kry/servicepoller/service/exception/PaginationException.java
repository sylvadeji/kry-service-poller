package com.kry.servicepoller.service.exception;

public class PaginationException extends RuntimeException {
    public PaginationException() {
    }

    public PaginationException(String message) {
        super(message);
    }

    public PaginationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
