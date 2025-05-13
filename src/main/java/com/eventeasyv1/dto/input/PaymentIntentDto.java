package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentIntentDto {

    @NotNull(message = "L'ID de la réservation est requis.")
    private Long reservationId;

    // Le montant sera généralement récupéré de la réservation côté backend
    // Mais on pourrait le passer pour vérification ou pour des cas spécifiques
    @NotNull(message = "Le montant est requis.")
    @Positive(message = "Le montant doit être positif.")
    private BigDecimal montant; // En devise principale (ex: EUR, USD)

    @NotNull(message = "La devise est requise.")
    private String devise; // Ex: "eur", "usd" (selon les codes ISO)
}
