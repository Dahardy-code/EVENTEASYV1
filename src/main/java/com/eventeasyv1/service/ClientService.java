package com.eventeasyv1.service;

import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.dto.ReservationDto; // <-- AJOUTER CET IMPORT
import org.springframework.security.core.Authentication;

import java.util.List; // <-- AJOUTER CET IMPORT

public interface ClientService {

    /**
     * Récupère les détails du client actuellement connecté.
     * @param authentication L'objet d'authentification de Spring Security.
     * @return ClientDto contenant les informations du client.
     */
    ClientDto getCurrentClientDetails(Authentication authentication);

    /**
     * Récupère toutes les réservations du client actuellement connecté.
     * @param authentication L'objet d'authentification de Spring Security.
     * @return Une liste de ReservationDto.
     */
    List<ReservationDto> getMyReservations(Authentication authentication);

    // Ajoutez d'autres signatures de méthodes spécifiques au client ici si nécessaire
}