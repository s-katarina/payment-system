package org.example.pspbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_url", nullable = false)
    private String serviceUrl;

    @ManyToMany(mappedBy = "paymentMethods", fetch = FetchType.LAZY)
    @JsonIgnore // Prevent recursion when serializing Merchant -> PaymentMethod -> Merchant
    private Set<Merchant> merchants = new HashSet<>();
}

