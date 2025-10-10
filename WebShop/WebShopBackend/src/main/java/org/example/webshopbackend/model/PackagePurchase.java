package org.example.webshopbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.model.enums.PurchaseStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackagePurchase {

    @Column
    private UUID id;  // Merchant ID

    @Column
    @ManyToOne
    private User customer;

    @Column
    @ManyToMany
    private PackageEntity packageEntity;

    @Column
    private LocalDateTime purchaseDate; // Merchant timestamp

    @Column
    private float amount;

    @Column
    private PurchaseStatus status;

}
