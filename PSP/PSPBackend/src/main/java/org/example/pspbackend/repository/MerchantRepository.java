package org.example.pspbackend.repository;

import org.example.pspbackend.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    // Note: Password comparison is done using BCrypt in MerchantApiKeyAuthenticationFilter
    // We don't query by password since it's hashed
    
    /**
     * Finds a merchant by ID and eagerly fetches payment methods using JOIN FETCH
     * This ensures payment methods are loaded even if EAGER fetch doesn't work as expected
     */
    @Query("SELECT DISTINCT m FROM Merchant m LEFT JOIN FETCH m.paymentMethods WHERE m.merchantId = :merchantId")
    Optional<Merchant> findByIdWithPaymentMethods(@Param("merchantId") String merchantId);
    
    /**
     * Finds all merchants and eagerly fetches payment methods using JOIN FETCH
     * This ensures payment methods are loaded for all merchants
     */
    @Query("SELECT DISTINCT m FROM Merchant m LEFT JOIN FETCH m.paymentMethods")
    java.util.List<Merchant> findAllWithPaymentMethods();
}

