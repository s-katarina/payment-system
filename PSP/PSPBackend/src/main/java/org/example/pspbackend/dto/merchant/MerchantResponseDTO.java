package org.example.pspbackend.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pspbackend.dto.paymentmethod.PaymentMethodSimpleDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponseDTO {
    private String merchantId;
    private String merchantName;
    private String currency;
    private String successUrl;
    private String failUrl;
    private String errorUrl;
    private Boolean active;
    private List<PaymentMethodSimpleDTO> paymentMethods;
}


