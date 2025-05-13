package com.eventeasyv1.dto;

import com.eventeasyv1.entities.enums.StatutReservation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDto {
    private Long id;
    private LocalDateTime dateReservation;
    private StatutReservation statut;
    private BigDecimal prixFinal;
    private LocalDateTime dateCreation;

    // Informations simplifiées du client
    private Long clientId;
    private String clientNomComplet; // Exemple: "Prenom Nom"

    // Informations simplifiées du service
    private Long serviceId;
    private String serviceNom; // Nom ou titre du service

    // Informations simplifiées du prestataire (via le service)
    private Long prestataireId;
    private String prestataireNomEntreprise;
}