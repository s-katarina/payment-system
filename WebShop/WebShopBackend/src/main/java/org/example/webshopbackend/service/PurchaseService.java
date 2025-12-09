package org.example.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.payment.BuyPackageResponseDTO;
import org.example.webshopbackend.exception.PackageDoesNotExistByIdException;
import org.example.webshopbackend.exception.user.UserDoesNotExistByIdException;
import org.example.webshopbackend.model.PackageEntity;
import org.example.webshopbackend.model.PackagePurchase;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.model.enums.PSPPaymentResultStatus;
import org.example.webshopbackend.model.enums.PurchaseStatus;
import org.example.webshopbackend.repository.PackagePurchaseRepository;
import org.example.webshopbackend.repository.PackageRepository;
import org.example.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private final PackagePurchaseRepository packagePurchaseRepository;
    private final PackageRepository packageRepository;

    public BuyPackageResponseDTO buyPackage(UUID userId, UUID packageId) {

        PackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageDoesNotExistByIdException(packageId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistByIdException(userId));

        // Save purchase to databse
        PackagePurchase purchase = new PackagePurchase();
        purchase.setCustomer(user);
        purchase.setPackageEntity(packageEntity);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setAmount(packageEntity.getPrice());
        purchase.setStatus(PurchaseStatus.INITIATED);
        PackagePurchase savedPurchase = packagePurchaseRepository.save(purchase);

        // Synchronous request to PSP
        // with merchantId, apiKey, merchantOrderId, amount, merchantTimestamp
        // Response from PSP with redirectURL to PSP

        String redirectLink = "";
        return new BuyPackageResponseDTO(redirectLink);
    }

    private void sendPurchaseRequestToPSP(PackagePurchase purchase) {
        // PSP URL from API Gateway
    }

    public void updatePurchaseStatus(UUID Purchase, PSPPaymentResultStatus status) {
        // Get message in MQ, update purchase status
    }

    public void getPurchases(UUID userId) {

    }

}
