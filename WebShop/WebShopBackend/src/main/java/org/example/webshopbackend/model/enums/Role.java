package org.example.webshopbackend.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
