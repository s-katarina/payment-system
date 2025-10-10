package org.example.webshopbackend.model.enums;

import lombok.Getter;

@Getter
public enum PurchaseStatus {

    INITIATED("INITIATED"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    ERROR("ERROR");


    private final String resultName;

    PurchaseStatus(String resultName) {
        this.resultName = resultName;
    }
}
