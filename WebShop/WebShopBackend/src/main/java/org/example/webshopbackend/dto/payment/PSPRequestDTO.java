package org.example.webshopbackend.dto.payment;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class PSPRequestDTO {
    private BigDecimal amount;
    private String merchantOrderId;
    private String merchantTimestamp;
}
