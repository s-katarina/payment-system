package org.example.pspbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.merchant.CreateMerchantRequestDTO;
import org.example.pspbackend.dto.merchant.CreateMerchantResponseDTO;
import org.example.pspbackend.dto.merchant.GeneratePasswordResponseDTO;
import org.example.pspbackend.dto.merchant.MerchantResponseDTO;
import org.example.pspbackend.dto.merchant.UpdateMerchantRequestDTO;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;
import org.example.pspbackend.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/merchant")
@PreAuthorize("hasRole('ADMIN')")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping
    public ResponseEntity<List<MerchantResponseDTO>> getAllMerchants() {
        log.info("Fetching all merchants");
        
        List<MerchantResponseDTO> merchants = merchantService.getAllMerchants();
        
        log.info("Found {} merchants", merchants.size());
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<MerchantResponseDTO> getMerchantById(@PathVariable String merchantId) {
        log.info("Fetching merchant with ID: {}", merchantId);
        
        MerchantResponseDTO merchant = merchantService.getMerchantById(merchantId);
        
        log.info("Merchant found with ID: {}", merchantId);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

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

    @GetMapping("/{merchantId}/payment-methods")
    public ResponseEntity<List<PaymentMethodResponseDTO>> getPaymentMethodsForMerchant(@PathVariable String merchantId) {
        log.info("Fetching payment methods for merchant with ID: {}", merchantId);
        
        List<PaymentMethodResponseDTO> paymentMethods = merchantService.getPaymentMethodsForMerchant(merchantId);
        
        log.info("Found {} payment methods for merchant with ID: {}", paymentMethods.size(), merchantId);
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }
}

