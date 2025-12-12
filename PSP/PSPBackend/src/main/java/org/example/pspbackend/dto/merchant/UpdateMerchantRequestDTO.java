package org.example.pspbackend.dto.merchant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMerchantRequestDTO {

    @NotBlank(message = "Merchant name is required")
    private String merchantName;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Success URL is required")
    private String successUrl;

    @NotBlank(message = "Fail URL is required")
    private String failUrl;

    @NotBlank(message = "Error URL is required")
    private String errorUrl;
}

