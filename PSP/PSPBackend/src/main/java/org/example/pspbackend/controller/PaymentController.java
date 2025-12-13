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

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(
            @RequestHeader(value = "X-Merchant-Id", required = true) String merchantId,
            @RequestHeader(value = "X-API-Key", required = true) String apiKey,
            @Valid @RequestBody CreatePaymentRequestDTO request) {
        
        log.info("Received payment creation request from merchant: {}", merchantId);
        
        // TODO: Validate API key authentication here
        // For now, we'll just use the merchant ID from header
        
        CreatePaymentResponseDTO response = paymentService.createPayment(request, merchantId);
        String redirectUrl = response.getRedirectUrl();
        
        log.info("Payment creation successful, redirect URL: {}", redirectUrl);
        return new ResponseEntity<>(redirectUrl, HttpStatus.CREATED);
    }

    @PostMapping("/initiate")
    public ResponseEntity<Void> initiatePayment(
            @RequestHeader(value = "X-Merchant-Id", required = true) String merchantId,
            @RequestParam(value = "paymentId", required = true) UUID paymentId) {
        
        log.info("Received payment initiation request for payment: {} from merchant: {}", paymentId, merchantId);
        
        paymentService.initiatePayment(paymentId, merchantId);
        
        log.info("Payment initiation successful for payment: {}", paymentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
