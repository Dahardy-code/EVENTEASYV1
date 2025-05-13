package com.eventeasyv1.dto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PromoDto {
    private Long id;
    private String codePromo;
    private String description;
    private BigDecimal pourcentageReduction;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean estActive;
}