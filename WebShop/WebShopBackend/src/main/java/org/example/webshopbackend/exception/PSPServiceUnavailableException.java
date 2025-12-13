package org.example.webshopbackend.exception;

public class PSPServiceUnavailableException extends RuntimeException {
    public PSPServiceUnavailableException(String message) {
        super(message);
    }

    public PSPServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

