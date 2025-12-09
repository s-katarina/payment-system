package org.example.webshopbackend.repository;

import org.example.webshopbackend.model.PackagePurchase;
import org.example.webshopbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PackagePurchaseRepository extends JpaRepository<PackagePurchase, UUID> {
    List<PackagePurchase> findByCustomer(User customer);
}
