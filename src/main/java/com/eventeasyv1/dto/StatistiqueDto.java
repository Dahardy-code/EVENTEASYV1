package com.eventeasyv1.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StatistiqueDto {
    private Long id;
    private String typeStatistique;
    private BigDecimal valeur;
    private LocalDate dateStatistique;

    public void setDate(LocalDate dateStatistique) {
        this.dateStatistique = dateStatistique;
    }
}