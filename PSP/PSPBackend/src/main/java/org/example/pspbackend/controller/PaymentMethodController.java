package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.paymentmethod.CreatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodServiceUrlRequestDTO;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.service.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentMethodResponseDTO> createPaymentMethod(
            @Valid @RequestBody CreatePaymentMethodRequestDTO request) {
        log.info("Creating payment method with name: {}", request.getName());
        
        PaymentMethodResponseDTO response = paymentMethodService.createPaymentMethod(request);
        
        log.info("Payment method created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethod(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentMethodRequestDTO request) {
        log.info("Updating payment method with ID: {}", id);
        
        PaymentMethodResponseDTO response = paymentMethodService.updatePaymentMethod(id, request);
        
        log.info("Payment method updated successfully with ID: {}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/service-url")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethodServiceUrl(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentMethodServiceUrlRequestDTO request) {
        log.info("Updating service URL for payment method with ID: {}", id);
        
        PaymentMethodResponseDTO response = paymentMethodService.updatePaymentMethodServiceUrl(id, request);
        
        log.info("Payment method service URL updated successfully with ID: {}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> getPaymentMethods() {
        // Get authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Check if user is ADMIN - return all payment methods
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            log.info("Admin fetching all payment methods");
            List<PaymentMethodResponseDTO> paymentMethods = paymentMethodService.getAllPaymentMethods();
            log.info("Found {} payment methods", paymentMethods.size());
            return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
        }

        // Otherwise, check if it's a merchant
        if (authentication.getPrincipal() instanceof Merchant) {
            Merchant merchant = (Merchant) authentication.getPrincipal();
            
            // Additional safety check: ensure merchant is active
            if (merchant.getActive() == null || !merchant.getActive()) {
                log.warn("Inactive merchant attempted to access payment methods: {}", merchant.getMerchantId());
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            log.info("Fetching payment methods for merchant: {}", merchant.getMerchantId());
            
            List<PaymentMethodResponseDTO> paymentMethods = paymentMethodService.getPaymentMethodsForMerchant(merchant);
            
            log.info("Found {} payment methods for merchant: {}", paymentMethods.size(), merchant.getMerchantId());
            return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}

