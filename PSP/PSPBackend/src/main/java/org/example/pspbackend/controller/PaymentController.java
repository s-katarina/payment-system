package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.payment.CreatePaymentRequestDTO;
import org.example.pspbackend.dto.payment.CreatePaymentResponseDTO;
import org.example.pspbackend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CreatePaymentResponseDTO> createPayment(
            @Valid @RequestBody CreatePaymentRequestDTO request) {
        log.info("Creating payment for order: {}", request.getMerchantOrderId());
        
        CreatePaymentResponseDTO response = paymentService.createPayment(request);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

