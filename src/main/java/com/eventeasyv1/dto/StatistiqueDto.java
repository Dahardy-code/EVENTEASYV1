package com.eventeasyv1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatistiqueDto {
    private Long id;
    private String typeStatistique;
    private String valeur; // Correspond au champ String de l'entité
    // private BigDecimal valeurNumerique; // Si vous utilisez un champ numérique dans l'entité
    private LocalDate periodeDebut;
    private LocalDate periodeFin;
    private LocalDate dateCalcul;
    private String details;
}