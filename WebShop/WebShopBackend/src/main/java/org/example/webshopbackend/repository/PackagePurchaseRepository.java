package org.example.webshopbackend.repository;

import org.example.webshopbackend.model.PackagePurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackagePurchaseRepository extends JpaRepository<PackagePurchase, UUID> {
}
