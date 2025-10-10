package org.example.webshopbackend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuyPackageResponseDTO {
    private String redirectUrl;
}
