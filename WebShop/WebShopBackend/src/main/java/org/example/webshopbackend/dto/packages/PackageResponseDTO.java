package org.example.webshopbackend.dto.packages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.model.PackageEntity;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PackageResponseDTO {

    private UUID id;
    private String packageName;
    private float price;

    public PackageResponseDTO(UUID id, String packageName, float price) {
        this.id = id;
        this.packageName = packageName;
        this.price = price;
    }

    public PackageResponseDTO(PackageEntity packageEntity) {
        this.id = packageEntity.getId();
        this.packageName = packageEntity.getPackageName();
        this.price = packageEntity.getPrice();
    }
}
