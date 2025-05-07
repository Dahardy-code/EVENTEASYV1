package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.dto.input.ReservationCreateDto; // Créez ce DTO
import com.eventeasyv1.service.ReservationService;   // Créez ce Service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService; // Injectez

    // Endpoint pour créer une réservation (par un client)
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')") // Seul un client peut créer une réservation
    public ResponseEntity<ReservationDto> createReservation(
            @Valid @RequestBody ReservationCreateDto reservationDto) {
        try {
            // TODO: Implémenter reservationService.createReservation(reservationDto);
            // Le service doit récupérer l'ID client connecté et valider la disponibilité, etc.
            ReservationDto createdReservation = reservationService.createReservation(reservationDto); // Exemple
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (Exception e) { // Adaptez les exceptions (ServiceNotFound, NotAvailable, etc.)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de créer la réservation.", e);
        }
    }

    // Endpoint pour voir une réservation spécifique (Client ou Prestataire concerné, ou Admin)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'PRESTATAIRE', 'ADMIN')") // Exemple d'autorisation
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        try {
            // TODO: Implémenter reservationService.getReservationByIdAndCheckAccess(id);
            // Le service doit vérifier si l'utilisateur connecté a le droit de voir cette réservation
            ReservationDto reservation = reservationService.getReservationById(id); // Exemple simple
            return ResponseEntity.ok(reservation);
        } catch (Exception e) { // Adaptez (NotFound, AccessDenied)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation non trouvée ou accès refusé.", e);
        }
    }

    // Ajoutez d'autres endpoints (ex: annuler une réservation)
}