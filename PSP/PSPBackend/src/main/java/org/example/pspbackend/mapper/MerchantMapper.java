package org.example.pspbackend.mapper;

import org.example.pspbackend.dto.merchant.CreateMerchantRequestDTO;
import org.example.pspbackend.dto.merchant.CreateMerchantResponseDTO;
import org.example.pspbackend.dto.merchant.GeneratePasswordResponseDTO;
import org.example.pspbackend.dto.merchant.UpdateMerchantRequestDTO;
import org.example.pspbackend.model.Merchant;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper {

    /**
     * Maps CreateMerchantRequestDTO to Merchant entity
     * Note: merchantId and merchantPassword should be set separately in service
     */
    public Merchant mapCreateRequestToMerchant(CreateMerchantRequestDTO dto) {
        Merchant merchant = new Merchant();
        merchant.setMerchantName(dto.getMerchantName());
        merchant.setCurrency(dto.getCurrency());
        merchant.setSuccessUrl(dto.getSuccessUrl());
        merchant.setFailUrl(dto.getFailUrl());
        merchant.setErrorUrl(dto.getErrorUrl());
        return merchant;
    }

    /**
     * Maps Merchant entity to CreateMerchantResponseDTO
     */
    public CreateMerchantResponseDTO mapMerchantToResponse(Merchant merchant) {
        CreateMerchantResponseDTO dto = new CreateMerchantResponseDTO();
        dto.setMerchantId(merchant.getMerchantId());
        dto.setMerchantName(merchant.getMerchantName());
        dto.setMerchantPassword(merchant.getMerchantPassword());
        dto.setCurrency(merchant.getCurrency());
        dto.setSuccessUrl(merchant.getSuccessUrl());
        dto.setFailUrl(merchant.getFailUrl());
        dto.setErrorUrl(merchant.getErrorUrl());
        return dto;
    }

    /**
     * Updates Merchant entity with data from UpdateMerchantRequestDTO
     * Only updates: name, currency, and URLs (not merchantId or merchantPassword)
     */
    public void updateMerchantFromDto(Merchant merchant, UpdateMerchantRequestDTO dto) {
        merchant.setMerchantName(dto.getMerchantName());
        merchant.setCurrency(dto.getCurrency());
        merchant.setSuccessUrl(dto.getSuccessUrl());
        merchant.setFailUrl(dto.getFailUrl());
        merchant.setErrorUrl(dto.getErrorUrl());
    }

    /**
     * Maps merchantId and newPassword to GeneratePasswordResponseDTO
     */
    public GeneratePasswordResponseDTO mapToGeneratePasswordResponse(String merchantId, String newPassword) {
        GeneratePasswordResponseDTO dto = new GeneratePasswordResponseDTO();
        dto.setMerchantId(merchantId);
        dto.setNewPassword(newPassword);
        return dto;
    }
}

