package org.example.webshopbackend.repository;

import org.example.webshopbackend.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<PackageEntity, UUID> {
}
