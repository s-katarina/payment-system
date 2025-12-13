package org.example.pspbackend.dto.paymentmethod;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Service URL is required")
    private String serviceUrl;
}


