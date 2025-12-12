package org.example.pspbackend.mapper;

import org.example.pspbackend.dto.paymentmethod.CreatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodRequestDTO;
import org.example.pspbackend.dto.paymentmethod.UpdatePaymentMethodServiceUrlRequestDTO;
import org.example.pspbackend.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    /**
     * Maps CreatePaymentMethodRequestDTO to PaymentMethod entity
     */
    public PaymentMethod mapCreateRequestToPaymentMethod(CreatePaymentMethodRequestDTO dto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(dto.getName());
        paymentMethod.setServiceName(dto.getServiceName());
        paymentMethod.setServiceUrl(dto.getServiceUrl());
        return paymentMethod;
    }

    /**
     * Maps PaymentMethod entity to PaymentMethodResponseDTO
     */
    public PaymentMethodResponseDTO mapPaymentMethodToResponse(PaymentMethod paymentMethod) {
        PaymentMethodResponseDTO dto = new PaymentMethodResponseDTO();
        dto.setId(paymentMethod.getId());
        dto.setName(paymentMethod.getName());
        dto.setServiceName(paymentMethod.getServiceName());
        dto.setServiceUrl(paymentMethod.getServiceUrl());
        return dto;
    }

    /**
     * Updates PaymentMethod entity with data from UpdatePaymentMethodRequestDTO
     * Only updates: name and serviceName
     */
    public void updatePaymentMethodFromDto(PaymentMethod paymentMethod, UpdatePaymentMethodRequestDTO dto) {
        paymentMethod.setName(dto.getName());
        paymentMethod.setServiceName(dto.getServiceName());
    }

    /**
     * Updates PaymentMethod entity service URL from UpdatePaymentMethodServiceUrlRequestDTO
     */
    public void updatePaymentMethodServiceUrlFromDto(PaymentMethod paymentMethod, UpdatePaymentMethodServiceUrlRequestDTO dto) {
        paymentMethod.setServiceUrl(dto.getServiceUrl());
    }
}

