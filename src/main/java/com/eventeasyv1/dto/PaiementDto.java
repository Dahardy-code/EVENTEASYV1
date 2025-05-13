package com.eventeasyv1.dto;

import com.eventeasyv1.entities.enums.StatutPaiement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaiementDto {
    private Long id;
    private BigDecimal montant;
    private LocalDateTime datePaiement;
    private String modePaiement;
    private StatutPaiement statut;
    private String idTransactionExterne;
    private LocalDateTime dateCreation;
    private Long reservationId;
    // Optionnel: Infos sur la réservation si besoin
    // private Long reservationClientId;
    // private Long reservationServiceId;
}