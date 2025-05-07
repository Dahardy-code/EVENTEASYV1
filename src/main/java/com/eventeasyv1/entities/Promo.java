package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_promo", length = 50, unique = true) // code promo devrait être unique
    private String codePromo;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "pourcentage_reduction", precision = 5, scale = 2) // DECIMAL(5,2)
    private BigDecimal pourcentageReduction;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    // TODO: Ajouter potentiellement des conditions d'utilisation (nombre max, services applicables...)
}