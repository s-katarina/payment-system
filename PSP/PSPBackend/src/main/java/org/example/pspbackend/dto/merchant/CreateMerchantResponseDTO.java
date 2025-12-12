package org.example.pspbackend.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodResponseDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantResponseDTO {

    private String merchantId;
    private String merchantName;
    private String merchantPassword; // API key
    private String currency;
    private String successUrl;
    private String failUrl;
    private String errorUrl;
    private List<PaymentMethodResponseDTO> paymentMethods;
}

