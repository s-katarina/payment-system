package org.example.webshopbackend.exception.user;

import java.util.UUID;

public class UserDoesNotExistByIdException extends  RuntimeException {

    public UserDoesNotExistByIdException(UUID userId) {
        super(String.format("User with id %s does not exist", userId));
    }
}
