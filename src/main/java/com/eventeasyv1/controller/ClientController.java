package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.dto.ReservationDto; // Importer si nécessaire
import com.eventeasyv1.service.ClientService;
import com.eventeasyv1.service.ReservationService; // Injecter si les réservations sont gérées ici
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('CLIENT')") // Sécuriser toutes les méthodes de ce contrôleur pour CLIENT
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Injecter ReservationService si la logique de récupération des résas est ici
    // @Autowired
    // private ReservationService reservationService;

    @GetMapping("/me")
    public ResponseEntity<ClientDto> getCurrentClientInfo() {
        try {
            ClientDto clientInfo = clientService.getCurrentClientDetails();
            return ResponseEntity.ok(clientInfo);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error fetching client info: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne serveur.", e);
        }
    }

    // Endpoint pour récupérer les réservations du client connecté
    @GetMapping("/me/reservations")
    public ResponseEntity<List<ReservationDto>> getMyReservations() {
        try {
            // TODO: Implémenter la logique dans ClientService ou ReservationService
            // List<ReservationDto> reservations = clientService.getMyReservations();
            List<ReservationDto> reservations = Collections.emptyList(); // Placeholder
            return ResponseEntity.ok(reservations);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error fetching client reservations: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    // Ajoutez ici d'autres endpoints spécifiques aux clients (profil, etc.)
}