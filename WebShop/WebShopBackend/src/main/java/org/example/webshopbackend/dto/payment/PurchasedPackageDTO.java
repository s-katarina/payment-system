package org.example.webshopbackend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.webshopbackend.model.enums.PurchaseStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchasedPackageDTO {
    private UUID purchaseId;
    private UUID packageId;
    private String packageName;
    private float price;
    private LocalDateTime purchaseDate;
    private PurchaseStatus status;
}


