package org.example.pspbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchantId;

    @Column(name = "merchant_password", nullable = false)
    private String merchantPassword;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "success_url", nullable = false)
    private String successUrl;

    @Column(name = "fail_url", nullable = false)
    private String failUrl;

    @Column(name = "error_url", nullable = false)
    private String errorUrl;
}

