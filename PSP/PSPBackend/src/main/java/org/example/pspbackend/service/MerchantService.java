package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.pspbackend.dto.merchant.CreateMerchantRequestDTO;
import org.example.pspbackend.dto.merchant.CreateMerchantResponseDTO;
import org.example.pspbackend.dto.merchant.GeneratePasswordResponseDTO;
import org.example.pspbackend.dto.merchant.UpdateMerchantRequestDTO;
import org.example.pspbackend.exception.MerchantNotFoundException;
import org.example.pspbackend.mapper.MerchantMapper;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int API_KEY_LENGTH = 32; // 32 bytes = 44 characters when base64 encoded

    @Transactional
    public CreateMerchantResponseDTO createMerchant(CreateMerchantRequestDTO request) {
        String merchantId = generateMerchantId();
        String merchantPassword = generateApiKey();
        Merchant merchant = merchantMapper.mapCreateRequestToMerchant(request);
        merchant.setMerchantId(merchantId);
        merchant.setMerchantPassword(merchantPassword);
        Merchant savedMerchant = merchantRepository.save(merchant);
        return merchantMapper.mapMerchantToResponse(savedMerchant);
    }

    @Transactional
    public CreateMerchantResponseDTO updateMerchant(String merchantId, UpdateMerchantRequestDTO request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found with ID: " + merchantId));

        merchantMapper.updateMerchantFromDto(merchant, request);
        Merchant updatedMerchant = merchantRepository.save(merchant);
        return merchantMapper.mapMerchantToResponse(updatedMerchant);
    }

    @Transactional
    public void deleteMerchant(String merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found with ID: " + merchantId));
        merchantRepository.delete(merchant);
    }

    @Transactional
    public GeneratePasswordResponseDTO generateNewPassword(String merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found with ID: " + merchantId));
        String newPassword = generateApiKey();
        merchant.setMerchantPassword(newPassword);

        merchantRepository.save(merchant);
        return merchantMapper.mapToGeneratePasswordResponse(merchantId, newPassword);
    }

    /**
     * Generates a unique merchant ID using UUID
     */
    private String generateMerchantId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a secure API key (merchant password) using cryptographically secure random bytes
     * Returns a base64-encoded string that can be used as an API key
     */
    private String generateApiKey() {
        byte[] randomBytes = new byte[API_KEY_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}

