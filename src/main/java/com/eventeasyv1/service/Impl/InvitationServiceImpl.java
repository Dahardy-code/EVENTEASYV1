package com.eventeasyv1.service.Impl;
import com.eventeasyv1.dao.ClientRepository; // Pour vérifier le propriétaire de l'événement
import com.eventeasyv1.dao.EvenementRepository;
import com.eventeasyv1.dao.InvitationRepository;
import com.eventeasyv1.dto.InvitationDto;
import com.eventeasyv1.dto.input.InvitationCreateDto;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Evenement;
import com.eventeasyv1.entities.Invitation;
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.InvitationService;
// import com.eventeasyv1.service.MailService; // Pour l'envoi d'email réel
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationServiceImpl implements InvitationService {

    private static final Logger log = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;
    private final EvenementRepository evenementRepository;
    private final ClientRepository clientRepository; // Pour vérifier le propriétaire de l'événement

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository,
                                 EvenementRepository evenementRepository,
                                 ClientRepository clientRepository) {
        this.invitationRepository = invitationRepository;
        this.evenementRepository = evenementRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public InvitationDto sendInvitation(Long evenementId, InvitationCreateDto invitationDto, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Evenement evenement = evenementRepository.findByIdAndClientId(evenementId, client.getId()) // Vérifie que l'événement appartient au client
                .orElseThrow(() -> new ResourceNotFoundException("Evenement", "id", evenementId + " pour ce client"));

        Invitation invitation = new Invitation();
        invitation.setEmail(invitationDto.getEmail());
        invitation.setMessage(invitationDto.getMessage());
        invitation.setEvenement(evenement);

        Invitation savedInvitation = invitationRepository.save(invitation);
        // TODO: Implémenter l'envoi d'email ici si nécessaire
        log.info("Invitation ID {} envoyée pour l'événement ID {} à {}", savedInvitation.getId(), evenementId, invitationDto.getEmail());
        return mapToDto(savedInvitation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvitationDto> getInvitationsForEvenement(Long evenementId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        // Vérifier que le client est propriétaire de l'événement avant de lister les invitations
        evenementRepository.findByIdAndClientId(evenementId, client.getId())
                .orElseThrow(() -> new AccessDeniedException("Vous n'êtes pas autorisé à voir les invitations pour cet événement."));

        List<Invitation> invitations = invitationRepository.findByEvenementIdOrderByDateEnvoiDesc(evenementId);
        return invitations.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelInvitation(Long evenementId, Long invitationId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Invitation invitation = invitationRepository.findByIdAndEvenementId(invitationId, evenementId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation", "id", invitationId + " pour l'événement " + evenementId));

        // Vérification supplémentaire que l'événement appartient au client
        if (!invitation.getEvenement().getClient().getId().equals(client.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à annuler cette invitation.");
        }

        invitationRepository.delete(invitation);
        log.info("Invitation ID {} annulée pour l'événement ID {}", invitationId, evenementId);
    }

    private InvitationDto mapToDto(Invitation invitation) {
        InvitationDto dto = new InvitationDto();
        dto.setId(invitation.getId());
        dto.setEmail(invitation.getEmail());
        dto.setMessage(invitation.getMessage());
        dto.setDateEnvoi(invitation.getDateEnvoi());
        if (invitation.getEvenement() != null) {
            dto.setEvenementId(invitation.getEvenement().getId());
        }
        return dto;
    }
}