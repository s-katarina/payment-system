package org.example.webshopbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.webshopbackend.dto.packages.PackageResponseDTO;
import org.example.webshopbackend.dto.payment.PurchasedPackageDTO;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.service.JWTService;
import org.example.webshopbackend.service.PackageService;
import org.example.webshopbackend.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private JWTService jwtService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("all")
    public ResponseEntity<List<PackageResponseDTO>> getPackages()
    {
        return new ResponseEntity<>(packageService.getAllPackages(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/purchased")
    public ResponseEntity<List<PurchasedPackageDTO>> getPurchasedPackages() {
        User user = (User) jwtService.getAuthenticatedUser();
        return new ResponseEntity<>(purchaseService.getPurchases(user.getId()), HttpStatus.OK);
    }

}
