package com.bukoz.cryptoexchange.exception;

import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import com.bukoz.cryptoexchange.exception.internal.InternalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalApiException.class)
    public ResponseEntity<String> handleInternalApiException(InternalApiException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<String> handleExternalApiException(ExternalApiException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
