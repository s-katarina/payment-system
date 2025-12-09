package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.pspbackend.dto.payment.CreatePaymentRequestDTO;
import org.example.pspbackend.dto.payment.CreatePaymentResponseDTO;
import org.example.pspbackend.exception.InvalidMerchantCredentialsException;
import org.example.pspbackend.exception.MerchantNotFoundException;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.model.Payment;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;

    @Value("${psp.base-url:http://localhost:8081}")
    private String pspBaseUrl;

    @Transactional
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {
        // Validate merchant credentials
        Merchant merchant = merchantRepository.findByMerchantIdAndMerchantPassword(
                request.getMerchantId(),
                request.getApiKey()
        ).orElseThrow(() -> new InvalidMerchantCredentialsException(
                "Invalid merchant credentials for merchant ID: " + request.getMerchantId()
        ));

        // Validate currency matches merchant's currency
        if (!merchant.getCurrency().equalsIgnoreCase(request.getCurrency())) {
            throw new IllegalArgumentException(
                    "Currency mismatch. Merchant supports: " + merchant.getCurrency()
            );
        }

        // Create and save payment
        Payment payment = new Payment();
        payment.setMerchantId(request.getMerchantId());
        payment.setMerchantOrderId(request.getMerchantOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setMerchantTimestamp(request.getMerchantTimestamp());
        payment.setCallbackUrl(request.getCallbackUrl());

        Payment savedPayment = paymentRepository.save(payment);

        // Generate redirect URL to PSP frontend where user will select payment method
        // Format: {baseUrl}/psp/payment/{paymentId}
        String redirectUrl = pspBaseUrl + "/psp/payment/" + savedPayment.getId().toString();

        return new CreatePaymentResponseDTO(redirectUrl);
    }
}

