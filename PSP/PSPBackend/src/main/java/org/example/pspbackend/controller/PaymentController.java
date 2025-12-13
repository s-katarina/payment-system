package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.payment.CreatePaymentRequestDTO;
import org.example.pspbackend.dto.payment.CreatePaymentResponseDTO;
import org.example.pspbackend.service.PaymentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponseDTO> createPayment(
            @RequestHeader(value = "X-Merchant-Id", required = true) String merchantId,
            @RequestHeader(value = "X-API-Key", required = true) String apiKey,
            @Valid @RequestBody CreatePaymentRequestDTO request) {
        
        log.info("Received payment creation request from merchant: {}", merchantId);
        
        // TODO: Validate API key authentication here
        // For now, we'll just use the merchant ID from header
        
        CreatePaymentResponseDTO response = paymentService.createPayment(request, merchantId);
        
        log.info("Payment creation successful, redirect URL: {}", response.getRedirectUrl());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
