package org.example.pspbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "paymentMethods")
@EqualsAndHashCode(exclude = "paymentMethods")
public class Merchant {

    @Id
    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchantId;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

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

    @Column(name = "active", nullable = false)
    private Boolean active = true; // Default to true for new merchants

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "merchant_payment_methods",
            joinColumns = @JoinColumn(name = "merchant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    @JsonIgnore // Prevent circular serialization (though we use DTOs, this is a safety measure)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();
}

