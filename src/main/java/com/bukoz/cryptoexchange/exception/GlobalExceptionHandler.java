package com.bukoz.cryptoexchange.exception;

import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.model.CurrencyErrorResponse;
import com.bukoz.cryptoexchange.model.InternalErrorResponse;
import com.bukoz.cryptoexchange.model.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<CurrencyErrorResponse> handleUnsupportedCoinException(UnsupportedCurrencyException ex) {
        log.warn("Unsupported currency exception: {}", ex.getMessage());
        CurrencyErrorResponse errorResponse = new CurrencyErrorResponse("Currency not found", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<String> handleExternalApiException(ExternalApiException ex) {
        log.warn("External API exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Argument not valid exception: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalErrorResponse> handleException(Exception ex) {
        log.warn("Exception :{}", ex.getMessage());
        InternalErrorResponse response = new InternalErrorResponse("Internal server error", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
