package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.merchant.CreateMerchantRequestDTO;
import org.example.pspbackend.dto.merchant.CreateMerchantResponseDTO;
import org.example.pspbackend.dto.merchant.GeneratePasswordResponseDTO;
import org.example.pspbackend.dto.merchant.UpdateMerchantRequestDTO;
import org.example.pspbackend.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/merchant")
@PreAuthorize("hasRole('ADMIN')")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/create")
    public ResponseEntity<CreateMerchantResponseDTO> createMerchant(
            @Valid @RequestBody CreateMerchantRequestDTO request) {
        log.info("Creating merchant with name: {}", request.getMerchantName());
        
        CreateMerchantResponseDTO response = merchantService.createMerchant(request);
        
        log.info("Merchant created successfully with ID: {}", response.getMerchantId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{merchantId}")
    public ResponseEntity<CreateMerchantResponseDTO> updateMerchant(
            @PathVariable String merchantId,
            @Valid @RequestBody UpdateMerchantRequestDTO request) {
        log.info("Updating merchant with ID: {}", merchantId);
        
        CreateMerchantResponseDTO response = merchantService.updateMerchant(merchantId, request);
        
        log.info("Merchant updated successfully with ID: {}", merchantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{merchantId}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable String merchantId) {
        log.info("Deleting merchant with ID: {}", merchantId);
        
        merchantService.deleteMerchant(merchantId);
        
        log.info("Merchant deleted successfully with ID: {}", merchantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{merchantId}/generate-password")
    public ResponseEntity<GeneratePasswordResponseDTO> generateNewPassword(@PathVariable String merchantId) {
        log.info("Generating new password for merchant with ID: {}", merchantId);
        
        GeneratePasswordResponseDTO response = merchantService.generateNewPassword(merchantId);
        
        log.info("New password generated for merchant with ID: {}", merchantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

