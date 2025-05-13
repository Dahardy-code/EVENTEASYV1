package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.dto.EvenementDto;
import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.dto.input.EvenementCreateDto;
import com.eventeasyv1.dto.input.EvenementUpdateDto;
import com.eventeasyv1.service.ClientService;
// import org.apache.tomcat.util.net.openssl.ciphers.Authentication; // MAUVAIS IMPORT - À SUPPRIMER
import com.eventeasyv1.service.EvenementService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication; // <-- BON IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Pour ResponseStatusException
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Pour gérer les exceptions proprement

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Endpoint pour obtenir les infos du client connecté
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')") // Sécuriser l'endpoint
    public ResponseEntity<ClientDto> getCurrentClientInfo(Authentication authentication) { // Injecter Authentication
        try {
            // Passer l'objet Authentication au service
            ClientDto clientInfo = clientService.getCurrentClientDetails(authentication);
            return ResponseEntity.ok(clientInfo);
        } catch (IllegalStateException e) { // Attraper l'exception si pas authentifié
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) { // Attraper d'autres exceptions potentielles du service
            // Logguez l'erreur côté serveur pour le débogage
            // log.error("Erreur lors de la récupération des détails du client", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur", e);
        }
    }

    // Endpoint pour récupérer les réservations du client connecté
    @GetMapping("/me/reservations")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ReservationDto>> getMyReservations(Authentication authentication) { // Injecter Authentication
        // Passer l'objet Authentication au service
        List<ReservationDto> reservations = clientService.getMyReservations(authentication);
        return ResponseEntity.ok(reservations);
    }

    @Autowired
    private EvenementService evenementService; // Injecter le service

// ---- Endpoints pour la Gestion des Événements du Client Connecté ----

    @PostMapping("/me/evenements")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<EvenementDto> createMyEvenement(@Valid @RequestBody EvenementCreateDto evenementDto, Authentication authentication) {
        EvenementDto createdEvenement = evenementService.createEvenement(evenementDto, authentication);
        return new ResponseEntity<>(createdEvenement, HttpStatus.CREATED);
    }

    @GetMapping("/me/evenements")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<EvenementDto>> getMyEvenements(Authentication authentication) {
        List<EvenementDto> evenements = evenementService.getMyEvenements(authentication);
        return ResponseEntity.ok(evenements);
    }

    @GetMapping("/me/evenements/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<EvenementDto> getMyEvenementById(@PathVariable Long id, Authentication authentication) {
        EvenementDto evenementDto = evenementService.getEvenementByIdForClient(id, authentication);
        return ResponseEntity.ok(evenementDto);
    }

    @PutMapping("/me/evenements/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<EvenementDto> updateMyEvenement(@PathVariable Long id, @Valid @RequestBody EvenementUpdateDto evenementDto, Authentication authentication) {
        EvenementDto updatedEvenement = evenementService.updateEvenement(id, evenementDto, authentication);
        return ResponseEntity.ok(updatedEvenement);
    }

    @DeleteMapping("/me/evenements/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteMyEvenement(@PathVariable Long id, Authentication authentication) {
        evenementService.deleteEvenement(id, authentication);
        return ResponseEntity.noContent().build();
    }
}