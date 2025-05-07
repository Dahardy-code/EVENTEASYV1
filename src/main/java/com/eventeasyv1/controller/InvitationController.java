package com.eventeasyv1.controller;

import com.eventeasyv1.dto.input.InvitationCreateDto; // Créez ce DTO
import com.eventeasyv1.service.InvitationService;   // Créez ce Service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class InvitationController {

    @Autowired
    private InvitationService invitationService; // Injectez

    // Endpoint pour un client pour envoyer une invitation pour un de ses événements
    @PostMapping("/evenements/{eventId}/invitations")
    @PreAuthorize("hasRole('CLIENT')") // Seul le client peut inviter à ses événements
    public ResponseEntity<?> sendInvitation(@PathVariable Long eventId,
                                            @Valid @RequestBody InvitationCreateDto invitationDto) {
        try {
            // TODO: Implémenter invitationService.sendInvitation(eventId, invitationDto);
            // La méthode service doit vérifier que l'événement appartient au client connecté
            // et récupérer l'ID client depuis SecurityContextHolder
            invitationService.sendInvitation(eventId, invitationDto); // Exemple
            return ResponseEntity.status(HttpStatus.CREATED).body("Invitation envoyée.");
        } catch (Exception e) { // Adaptez les exceptions
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible d'envoyer l'invitation.", e);
        }
    }

    // Ajoutez d'autres endpoints si nécessaire (ex: voir les invitations envoyées)
}