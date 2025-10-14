package org.example.webshopbackend.model;

import jakarta.persistence.*;
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
@Entity
public class PackagePurchase {

    @Id
    @GeneratedValue
    private UUID id;  // Merchant ID

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "package_entity_id", nullable = false)
    private PackageEntity packageEntity;

    @Column
    private LocalDateTime purchaseDate; // Merchant timestamp

    @Column
    private float amount;

    @Column
    private PurchaseStatus status;

}
