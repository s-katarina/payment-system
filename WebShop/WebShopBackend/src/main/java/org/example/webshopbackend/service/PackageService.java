package org.example.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.packages.PackageResponseDTO;
import org.example.webshopbackend.mapper.PackageMapper;
import org.example.webshopbackend.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    public List<PackageResponseDTO> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(packageMapper::mapToPackageResponseDTO)
        .toList();
    }


}