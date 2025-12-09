package org.example.pspbackend.repository;

import org.example.pspbackend.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    Optional<Merchant> findByMerchantIdAndMerchantPassword(String merchantId, String merchantPassword);
}

