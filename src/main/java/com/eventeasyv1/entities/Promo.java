package com.eventeasyv1.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promos")
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "code_promo", nullable = false, unique = true, length = 50)
    private String codePromo;

    @Lob
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    @DecimalMax(value = "100.00", inclusive = true)
    @Column(name = "pourcentage_reduction", precision = 5, scale = 2)
    private BigDecimal pourcentageReduction;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "est_active", nullable = false)
    private boolean estActive = true; // Par défaut active lors de la création
}
