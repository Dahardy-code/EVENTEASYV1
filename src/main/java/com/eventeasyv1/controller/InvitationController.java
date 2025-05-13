package com.eventeasyv1.controller;

import com.eventeasyv1.dto.InvitationDto;
import com.eventeasyv1.dto.input.InvitationCreateDto;
import com.eventeasyv1.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements/{evenementId}/invitations") // Invitations imbriquées sous événements
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')") // Seul le client propriétaire de l'événement peut envoyer
    public ResponseEntity<InvitationDto> sendInvitation(
            @PathVariable Long evenementId,
            @Valid @RequestBody InvitationCreateDto invitationDto,
            Authentication authentication) {
        InvitationDto sentInvitation = invitationService.sendInvitation(evenementId, invitationDto, authentication);
        return new ResponseEntity<>(sentInvitation, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')") // Seul le client propriétaire peut voir les invitations
    public ResponseEntity<List<InvitationDto>> getInvitationsForEvenement(
            @PathVariable Long evenementId,
            Authentication authentication) {
        List<InvitationDto> invitations = invitationService.getInvitationsForEvenement(evenementId, authentication);
        return ResponseEntity.ok(invitations);
    }

    @DeleteMapping("/{invitationId}")
    @PreAuthorize("hasRole('CLIENT')") // Seul le client propriétaire peut annuler
    public ResponseEntity<Void> cancelInvitation(
            @PathVariable Long evenementId,
            @PathVariable Long invitationId,
            Authentication authentication) {
        invitationService.cancelInvitation(evenementId, invitationId, authentication);
        return ResponseEntity.noContent().build();
    }
}