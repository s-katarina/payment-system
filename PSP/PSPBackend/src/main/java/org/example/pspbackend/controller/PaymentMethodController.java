package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.paymentmethod.CreatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodServiceUrlRequestDTO;
import org.example.pspbackend.service.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}

