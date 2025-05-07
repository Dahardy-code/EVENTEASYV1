package com.eventeasyv1.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaiementDto {
    private Long id;
    private BigDecimal montant;
    private LocalDateTime datePaiement;
    private String modePaiement; // ex: CARTE, PAYPAL, ESPECES
    private Long reservationId;
    private String referenceExterne; // Ex: ID de transaction Stripe/Paypal
}