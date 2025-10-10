package org.example.webshopbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.webshopbackend.dto.packages.PackageResponseDTO;
import org.example.webshopbackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @PreAuthorize("hasPermission('CUSTOMER')")
    @GetMapping("all")
    public ResponseEntity<List<PackageResponseDTO>> getPackages()
    {
        return new ResponseEntity<>(packageService.getAllPackages(), HttpStatus.OK);
    }



}
