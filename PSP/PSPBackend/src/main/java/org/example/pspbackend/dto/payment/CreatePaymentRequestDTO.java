package org.example.pspbackend.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequestDTO {

    @NotBlank(message = "Merchant ID is required")
    private String merchantId;

    @NotBlank(message = "API Key is required")
    private String apiKey;

    @NotBlank(message = "Merchant Order ID is required")
    private String merchantOrderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Merchant Timestamp is required")
    private String merchantTimestamp;

    @NotBlank(message = "Callback URL is required")
    private String callbackUrl;
}

