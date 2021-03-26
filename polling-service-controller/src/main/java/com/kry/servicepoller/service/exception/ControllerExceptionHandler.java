package com.kry.servicepoller.service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String PAGINATION_ERROR = "4406";


    @ExceptionHandler(value = {PaginationException.class})
    protected ResponseEntity<Object> handlePaginationException(PaginationException ex, WebRequest request) {
        logger.trace(ex.getMessage(), ex);
        return response(PAGINATION_ERROR, "Invalid pagination arguments: ",
                HttpStatus.BAD_REQUEST, ex, request);
    }

    private ResponseEntity<Object> response(String code, String msg, HttpStatus status, Exception ex,
                                            WebRequest request) {
        ApiError error = ApiError.builder().status(status.value()).message(msg +
                ex.getMessage()).type("ApiError").code(code).build();
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }
}
