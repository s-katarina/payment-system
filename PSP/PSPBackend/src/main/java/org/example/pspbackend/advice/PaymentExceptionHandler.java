package org.example.pspbackend.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.exception.InvalidMerchantCredentialsException;
import org.example.pspbackend.exception.MerchantNotFoundException;
import org.example.pspbackend.exception.PaymentMethodNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(InvalidMerchantCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidMerchantCredentials(
            InvalidMerchantCredentialsException ex) {
        log.error("Invalid merchant credentials: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid merchant credentials");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMerchantNotFound(
            MerchantNotFoundException ex) {
        log.error("Merchant not found: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Merchant not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePaymentMethodNotFound(
            PaymentMethodNotFoundException ex) {
        log.error("Payment method not found: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Payment method not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid argument");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

