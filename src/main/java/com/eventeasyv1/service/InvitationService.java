package com.eventeasyv1.service;

import com.eventeasyv1.dto.input.InvitationCreateDto;
import com.eventeasyv1.entities.Evenement;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class InvitationService {
    public void sendInvitation(Long eventId, @Valid InvitationCreateDto invitationDto) throws InterruptedException {
        // Validation si l'evenement existe, si l'utilisateur connecte est bien le proprietaire de l'evenement
        //       et si l'invite n'a pas deja ete invitee

        // R cup ration de l'utilisateur connecte
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long clientId = Long.parseLong(auth.getName());

        // R cup ration de l'evenement
        SimpleJpaRepository evenementRepository = null;
        Evenement evenement = evenementRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evenement non trouve"));

        // Validation si l'utilisateur connecte est bien le proprietaire de l'evenement
        if (!evenement.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n' tes pas le proprietaire de cet evenement");
        }

        // Validation si l'invite n'a pas deja ete invitee
        Object invitationRepository = null;
        if (invitationRepository.notify()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette personne a deja ete invitee a cet evenement");
        }
        System.out.println("Envoi d'invitation non implemente");
    }
}
