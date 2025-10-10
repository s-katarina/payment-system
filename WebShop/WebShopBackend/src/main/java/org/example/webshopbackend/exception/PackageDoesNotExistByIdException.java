package org.example.webshopbackend.exception;

import java.util.UUID;

public class PackageDoesNotExistByIdException extends RuntimeException {

    public PackageDoesNotExistByIdException(UUID id) {
        super(String.format("Package with id %s does not exists", id));
    }

}
