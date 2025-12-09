package org.example.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webshopbackend.dto.payment.BuyPackageResponseDTO;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.service.JWTService;
import org.example.webshopbackend.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final JWTService jwtService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/package")
    public ResponseEntity<BuyPackageResponseDTO> buyPackage(@RequestParam UUID packageId) {

        User user = (User) jwtService.getAuthenticatedUser();

        return new ResponseEntity<>(purchaseService.buyPackage(user.getId(), packageId), HttpStatus.OK);
    }
}
