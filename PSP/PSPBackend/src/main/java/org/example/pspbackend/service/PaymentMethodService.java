package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.pspbackend.dto.paymentmethod.CreatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodServiceUrlRequestDTO;
import org.example.pspbackend.exception.PaymentMethodNotFoundException;
import org.example.pspbackend.mapper.PaymentMethodMapper;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.model.PaymentMethod;
import org.example.pspbackend.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Transactional
    public PaymentMethodResponseDTO createPaymentMethod(CreatePaymentMethodRequestDTO request) {
        PaymentMethod paymentMethod = paymentMethodMapper.mapCreateRequestToPaymentMethod(request);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.mapPaymentMethodToResponse(savedPaymentMethod);
    }

    @Transactional
    public PaymentMethodResponseDTO updatePaymentMethod(UUID id, UpdatePaymentMethodRequestDTO request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found with ID: " + id));

        paymentMethodMapper.updatePaymentMethodFromDto(paymentMethod, request);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.mapPaymentMethodToResponse(updatedPaymentMethod);
    }

    @Transactional
    public PaymentMethodResponseDTO updatePaymentMethodServiceUrl(UUID id, UpdatePaymentMethodServiceUrlRequestDTO request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found with ID: " + id));

        paymentMethodMapper.updatePaymentMethodServiceUrlFromDto(paymentMethod, request);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.mapPaymentMethodToResponse(updatedPaymentMethod);
    }

    /**
     * Gets all payment methods for the authenticated merchant
     */
    public List<PaymentMethodResponseDTO> getPaymentMethodsForMerchant(Merchant merchant) {
        // Get payment methods from merchant's ManyToMany relationship
        return merchant.getPaymentMethods().stream()
                .map(paymentMethodMapper::mapPaymentMethodToResponse)
                .collect(Collectors.toList());
    }
}

