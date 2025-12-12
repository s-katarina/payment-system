package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.pspbackend.dto.payment.CreatePaymentRequestDTO;
import org.example.pspbackend.dto.payment.CreatePaymentResponseDTO;
import org.example.pspbackend.exception.InvalidMerchantCredentialsException;
import org.example.pspbackend.exception.MerchantInactiveException;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.model.Payment;
import org.example.pspbackend.model.enums.PaymentStatus;
import org.example.pspbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${psp.base-url:http://localhost:8081}")
    private String pspBaseUrl;

    @Transactional
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {
        // Get authenticated merchant from SecurityContext (set by MerchantApiKeyAuthenticationFilter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof Merchant)) {
            throw new InvalidMerchantCredentialsException("Merchant not authenticated");
        }

        Merchant merchant = (Merchant) authentication.getPrincipal();

        // Check if merchant is active
        if (merchant.getActive() == null || !merchant.getActive()) {
            throw new MerchantInactiveException("Merchant is inactive and cannot process payments");
        }

        // Create and save payment
        Payment payment = new Payment();
        payment.setMerchantId(merchant.getMerchantId());
        payment.setMerchantOrderId(request.getMerchantOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(merchant.getCurrency()); // Use currency from merchant entity
        payment.setMerchantTimestamp(request.getMerchantTimestamp());
        payment.setCreatedTimestamp(Instant.now().toString()); // Generate ISO timestamp on backend
        payment.setCallbackUrl(request.getCallbackUrl());
        payment.setPaymentStatus(PaymentStatus.INITIATED); // Set status to INITIATED on creation

        Payment savedPayment = paymentRepository.save(payment);

        // Generate redirect URL to PSP frontend where user will select payment method
        // Format: {baseUrl}/psp/payment/{paymentId}
        String redirectUrl = pspBaseUrl + "/psp/payment/" + savedPayment.getId().toString();

        return new CreatePaymentResponseDTO(redirectUrl);
    }
}

