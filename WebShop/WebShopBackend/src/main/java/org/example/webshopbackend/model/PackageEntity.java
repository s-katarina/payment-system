package org.example.webshopbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@Getter
@Setter
public class PackageEntity {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID id;

    @Column
    private String packageName;

    private float price;

}
