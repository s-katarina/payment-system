package org.example.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webshopbackend.dto.payment.BuyPackageResponseDTO;
import org.example.webshopbackend.dto.payment.PurchasedPackageDTO;
import org.example.webshopbackend.exception.PackageDoesNotExistByIdException;
import org.example.webshopbackend.exception.PSPServiceUnavailableException;
import org.example.webshopbackend.exception.user.UserDoesNotExistByIdException;
import org.example.webshopbackend.model.PackageEntity;
import org.example.webshopbackend.model.PackagePurchase;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.model.enums.PSPPaymentResultStatus;
import org.example.webshopbackend.model.enums.PurchaseStatus;
import org.example.webshopbackend.repository.PackagePurchaseRepository;
import org.example.webshopbackend.repository.PackageRepository;
import org.example.webshopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PurchaseService {

    private final UserRepository userRepository;
    private final PackagePurchaseRepository packagePurchaseRepository;
    private final PackageRepository packageRepository;
    private final RestTemplate restTemplate;

    @Value("${psp.api.url:http://localhost:8090/api/v1}")
    private String pspApiUrl;

    @Value("${psp.api.payment.create.endpoint:/payment/create}")
    private String paymentCreateEndpoint;

    @Value("${psp.merchant.id}")
    private String merchantId;

    @Value("${psp.merchant.api.key}")
    private String apiKey;

    public BuyPackageResponseDTO buyPackage(UUID userId, UUID packageId) {

        PackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageDoesNotExistByIdException(packageId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistByIdException(userId));

        // Save purchase to database
        PackagePurchase purchase = new PackagePurchase();
        purchase.setCustomer(user);
        purchase.setPackageEntity(packageEntity);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setAmount(packageEntity.getPrice());
        purchase.setStatus(PurchaseStatus.INITIATED);
        PackagePurchase savedPurchase = packagePurchaseRepository.save(purchase);

        // Call PSP payment creation endpoint
        String redirectUrl = createPaymentInPSP(savedPurchase);

        return new BuyPackageResponseDTO(redirectUrl);
    }

    private String createPaymentInPSP(PackagePurchase purchase) {
        log.info("Creating payment in PSP for purchase: {}", purchase.getId());

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("merchantOrderId", purchase.getId().toString());
        requestBody.put("amount", purchase.getAmount());
        requestBody.put("merchantTimestamp", Instant.now().toString());
        requestBody.put("callbackUrl", "http://localhost:8080/api/v1/purchase/callback"); // TODO: Configure properly

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Merchant-Id", merchantId);
        headers.set("X-API-Key", apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Call PSP payment creation endpoint
            String paymentCreateUrl = pspApiUrl + paymentCreateEndpoint;
            ResponseEntity<String> response = restTemplate.exchange(
                    paymentCreateUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String redirectUrl = response.getBody();
            log.info("Payment created in PSP, redirect URL: {}", redirectUrl);
            return redirectUrl != null ? redirectUrl : "";
        } catch (HttpClientErrorException e) {
            // Handle unauthorized/forbidden errors from PSP (likely due to invalid merchant credentials or service unavailable)
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.error("PSP service authentication failed (401/403) for purchase: {}. Error: {}", purchase.getId(), e.getMessage());
                throw new PSPServiceUnavailableException("Payment service unavailable at the moment");
            }
            log.error("HTTP error creating payment in PSP: {}", e.getMessage(), e);
            throw new PSPServiceUnavailableException("Payment service unavailable at the moment", e);
        } catch (RestClientException e) {
            // Handle connection errors, timeouts, etc.
            log.error("Connection error creating payment in PSP: {}", e.getMessage(), e);
            throw new PSPServiceUnavailableException("Payment service unavailable at the moment", e);
        } catch (Exception e) {
            log.error("Unexpected error creating payment in PSP: {}", e.getMessage(), e);
            throw new PSPServiceUnavailableException("Payment service unavailable at the moment", e);
        }
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
