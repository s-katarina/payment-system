package org.example.webshopbackend.exception.user;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format("Username %s already exists", username));
    }

}
