package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.dto.input.ReservationCreateDto;
import com.eventeasyv1.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')") // Seul un client peut créer une réservation
    public ResponseEntity<ReservationDto> createReservation(
            @Valid @RequestBody ReservationCreateDto reservationCreateDto,
            Authentication authentication) {
        ReservationDto createdReservation = reservationService.createReservation(reservationCreateDto, authentication);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    // Un client peut voir ses réservations, un prestataire celles de ses services, un admin toutes.
    // La logique de vérification est dans le service getReservationByIdForUser
    @PreAuthorize("isAuthenticated()") // Au minimum être authentifié
    public ResponseEntity<ReservationDto> getReservationById(
            @PathVariable Long id,
            Authentication authentication) {
        ReservationDto reservationDto = reservationService.getReservationByIdForUser(id, authentication);
        return ResponseEntity.ok(reservationDto);
    }
}