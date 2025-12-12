package org.example.pspbackend.model.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    INITIATED("INITIATED"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    ERROR("ERROR"),
    CANCEL("CANCEL");

    private final String statusName;

    PaymentStatus(String statusName) {
        this.statusName = statusName;
    }
}

