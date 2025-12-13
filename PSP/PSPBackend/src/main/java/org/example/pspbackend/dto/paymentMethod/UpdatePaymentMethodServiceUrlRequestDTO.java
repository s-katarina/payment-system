package org.example.pspbackend.dto.paymentmethod;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentMethodServiceUrlRequestDTO {

    @NotBlank(message = "Service URL is required")
    private String serviceUrl;
}


