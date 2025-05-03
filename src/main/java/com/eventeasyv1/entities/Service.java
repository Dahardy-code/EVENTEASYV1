package com.eventeasyv1.entities;

import jakarta.persistence.*; // Import general jakarta.persistence package
import lombok.Getter; // Using Lombok for less boilerplate
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter // Lombok annotation
@Setter // Lombok annotation
@NoArgsConstructor // Lombok annotation
@AllArgsConstructor // Lombok annotation
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;

    @Column(nullable = false) // Add constraints
    private String nom;

    @Column(columnDefinition = "TEXT") // Better for longer descriptions
    private String description;

    @Column(nullable = false)
    private double prix; // Consider using BigDecimal for currency

    @ManyToOne(fetch = FetchType.LAZY) // Relationship to Prestataire
    @JoinColumn(name = "prestataire_id", nullable = false) // Foreign key column
    private Prestataire prestataire;

    // Constructeur sans ID peut être utile (Lombok @NoArgsConstructor s'en charge si besoin)
    // Lombok @AllArgsConstructor gère le constructeur avec tous les champs

    // Lombok @Getter and @Setter handle all getters and setters automatically
    // No need to write them manually if you have Lombok configured

}