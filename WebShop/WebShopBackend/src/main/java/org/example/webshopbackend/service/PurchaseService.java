package org.example.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.payment.BuyPackageResponseDTO;
import org.example.webshopbackend.dto.payment.PurchasedPackageDTO;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        // with merchantId, apiKey, merchantOrderId, amount, currency (set here in webshop), merchantTimestamp
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

    public List<PurchasedPackageDTO> getPurchases(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistByIdException(userId));

        List<PackagePurchase> purchases = packagePurchaseRepository.findByCustomer(user);

        return purchases.stream()
                .map(purchase -> {
                    PurchasedPackageDTO dto = new PurchasedPackageDTO();
                    dto.setPurchaseId(purchase.getId());
                    dto.setPackageId(purchase.getPackageEntity().getId());
                    dto.setPackageName(purchase.getPackageEntity().getPackageName());
                    dto.setPrice(purchase.getAmount());
                    dto.setPurchaseDate(purchase.getPurchaseDate());
                    dto.setStatus(purchase.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
