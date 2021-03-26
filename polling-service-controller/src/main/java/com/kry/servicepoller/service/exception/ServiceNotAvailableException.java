package com.kry.servicepoller.service.exception;

import javax.xml.bind.ValidationException;

public class ServiceNotAvailableException extends ValidationException {
    public ServiceNotAvailableException(String message) {
        super(message);
    }

    public ServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

