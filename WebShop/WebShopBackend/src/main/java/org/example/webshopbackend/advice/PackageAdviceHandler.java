package org.example.webshopbackend.advice;

import org.example.webshopbackend.exception.PackageDoesNotExistByIdException;
import org.example.webshopbackend.exception.PSPServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PackageAdviceHandler {

    @ExceptionHandler(PackageDoesNotExistByIdException.class)
    public ResponseEntity<String> handlePackageDoesNotExist(PackageDoesNotExistByIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PSPServiceUnavailableException.class)
    public ResponseEntity<String> handlePSPServiceUnavailable(PSPServiceUnavailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

}