package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long id;
    private LocalDateTime dateReservation;
    private String statut;
    private Long clientId;
    private String clientNom; // Pour affichage facile
    private Long serviceId;
    private String serviceTitre; // Pour affichage facile
    private Long prestataireId; // Pour référence facile
}