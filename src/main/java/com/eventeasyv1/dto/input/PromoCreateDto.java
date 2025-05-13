package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PromoCreateDto {

    @NotBlank(message = "Le code promo est obligatoire.")
    @Size(max = 50, message = "Le code promo ne peut pas dépasser 50 caractères.")
    private String codePromo;

    private String description;

    @NotNull(message = "Le pourcentage de réduction est obligatoire.")
    @DecimalMin(value = "0.01", inclusive = true, message = "La réduction doit être d'au moins 0.01.")
    @DecimalMax(value = "100.00", inclusive = true, message = "La réduction ne peut pas dépasser 100.00.")
    private BigDecimal pourcentageReduction;

    @NotNull(message = "La date de début est obligatoire.")
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé.")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire.")
    @Future(message = "La date de fin doit être dans le futur.")
    private LocalDate dateFin;

    private Boolean estActive = true; // Par défaut
}
