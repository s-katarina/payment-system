package org.example.pspbackend.model.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    CREATED("CREATED"),
    INITIATED("INITIATED"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    ERROR("ERROR"),
    CANCELLED("CANCELLED");

    private final String statusName;

    PaymentStatus(String statusName) {
        this.statusName = statusName;
    }
}


