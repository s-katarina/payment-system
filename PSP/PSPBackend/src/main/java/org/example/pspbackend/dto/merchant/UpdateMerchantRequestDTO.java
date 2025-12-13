package org.example.pspbackend.dto.merchant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMerchantRequestDTO {

    private String merchantName;

    private String currency;

    private String successUrl;

    private String failUrl;

    private String errorUrl;

    private Boolean active;

    private List<UUID> paymentMethodIds;
}

