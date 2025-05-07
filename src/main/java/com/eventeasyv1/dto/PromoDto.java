package com.eventeasyv1.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromoDto {
    private Long id;
    private String codePromo;
    private String description;
    private BigDecimal pourcentageReduction;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean active; // Calculé ou stocké
}