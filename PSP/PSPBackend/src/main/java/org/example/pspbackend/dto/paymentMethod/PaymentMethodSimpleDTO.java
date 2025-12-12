package org.example.pspbackend.dto.paymentmethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodSimpleDTO {
    private UUID id;
    private String name;
    private String serviceName;
}

