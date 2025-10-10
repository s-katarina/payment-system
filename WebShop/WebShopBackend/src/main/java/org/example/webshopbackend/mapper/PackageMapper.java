package org.example.webshopbackend.mapper;

import org.example.webshopbackend.dto.packages.PackageResponseDTO;
import org.example.webshopbackend.model.PackageEntity;
import org.springframework.stereotype.Component;

@Component
public class PackageMapper {

    public PackageResponseDTO mapToPackageResponseDTO(PackageEntity packageEntity) {
        PackageResponseDTO response = new PackageResponseDTO();
        response.setId(packageEntity.getId());
        response.setPackageName(packageEntity.getPackageName());
        response.setPrice(packageEntity.getPrice());
        return response;
    };

}
