package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.payment.CreatePaymentRequestDTO;
import org.example.pspbackend.dto.payment.CreatePaymentResponseDTO;
import org.example.pspbackend.exception.MerchantNotFoundException;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.model.Payment;
import org.example.pspbackend.model.enums.PaymentStatus;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;

    @Value("${psp.frontend.url}")
    private String pspFrontendUrl;

    @Transactional
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO request, String merchantId) {
        log.info("Creating payment for merchant: {}", merchantId);

        // Validate merchant exists and is active
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found with ID: " + merchantId));

        if (merchant.getActive() == null || !merchant.getActive()) {
            throw new IllegalArgumentException("Merchant is not active");
        }

        // Create payment entity
        Payment payment = new Payment();
        payment.setMerchantId(merchantId);
        payment.setMerchantOrderId(request.getMerchantOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(merchant.getCurrency());
        payment.setMerchantTimestamp(request.getMerchantTimestamp());
        payment.setCallbackUrl(request.getCallbackUrl());
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.setCreatedTimestamp(Instant.now().toString());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", savedPayment.getId());

        // Generate redirect URL to PSP frontend payment method selection page
        String redirectUrl = String.format("%s/payment/%s?merchantId=%s", 
                pspFrontendUrl, savedPayment.getId(), merchantId);

        log.info("Generated redirect URL: {}", redirectUrl);

        return new CreatePaymentResponseDTO(redirectUrl);
    }
}
