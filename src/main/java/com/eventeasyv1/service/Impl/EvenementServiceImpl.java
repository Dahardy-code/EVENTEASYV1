package com.eventeasyv1.service.impl;
import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.EvenementRepository;
// import com.eventeasyv1.dao.InvitationRepository; // Si vous mappez les invitations
// import com.eventeasyv1.dto.InvitationDto; // Si vous mappez les invitations
import com.eventeasyv1.dto.EvenementDto;
import com.eventeasyv1.dto.input.EvenementCreateDto;
import com.eventeasyv1.dto.input.EvenementUpdateDto;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Evenement;
// import com.eventeasyv1.entities.Invitation; // Si vous mappez les invitations
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.EvenementService;
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
public class EvenementServiceImpl implements EvenementService {

    private static final Logger log = LoggerFactory.getLogger(EvenementServiceImpl.class);

    private final EvenementRepository evenementRepository;
    private final ClientRepository clientRepository;
    // private final InvitationServiceImpl invitationMapper; // Si vous mappez les invitations

    @Autowired
    public EvenementServiceImpl(EvenementRepository evenementRepository,
                                ClientRepository clientRepository
            /*, InvitationServiceImpl invitationMapper */) {
        this.evenementRepository = evenementRepository;
        this.clientRepository = clientRepository;
        // this.invitationMapper = invitationMapper;
    }

    @Override
    @Transactional
    public EvenementDto createEvenement(EvenementCreateDto evenementDto, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Evenement evenement = new Evenement();
        evenement.setNom(evenementDto.getNom());
        evenement.setDescription(evenementDto.getDescription());
        evenement.setDateEvenement(evenementDto.getDateEvenement());
        evenement.setLieu(evenementDto.getLieu());
        evenement.setClient(client);

        Evenement savedEvenement = evenementRepository.save(evenement);
        log.info("Événement ID {} créé par client ID {}", savedEvenement.getId(), client.getId());
        return mapToDto(savedEvenement);
    }

    @Override
    @Transactional(readOnly = true)
    public EvenementDto getEvenementByIdForClient(Long evenementId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Evenement evenement = evenementRepository.findByIdAndClientId(evenementId, client.getId()) // Utilise la méthode du repo
                .orElseThrow(() -> new ResourceNotFoundException("Evenement", "id", evenementId + " pour ce client"));

        return mapToDto(evenement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDto> getMyEvenements(Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        List<Evenement> evenements = evenementRepository.findByClientIdOrderByDateEvenementDesc(client.getId());
        return evenements.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvenementDto updateEvenement(Long evenementId, EvenementUpdateDto evenementDto, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Evenement evenement = evenementRepository.findByIdAndClientId(evenementId, client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Evenement", "id", evenementId + " pour ce client"));

        if (evenementDto.getNom() != null) evenement.setNom(evenementDto.getNom());
        if (evenementDto.getDescription() != null) evenement.setDescription(evenementDto.getDescription());
        if (evenementDto.getDateEvenement() != null) evenement.setDateEvenement(evenementDto.getDateEvenement());
        if (evenementDto.getLieu() != null) evenement.setLieu(evenementDto.getLieu());

        Evenement updatedEvenement = evenementRepository.save(evenement);
        log.info("Événement ID {} mis à jour par client ID {}", updatedEvenement.getId(), client.getId());
        return mapToDto(updatedEvenement);
    }

    @Override
    @Transactional
    public void deleteEvenement(Long evenementId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        Evenement evenement = evenementRepository.findByIdAndClientId(evenementId, client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Evenement", "id", evenementId + " pour ce client"));

        evenementRepository.delete(evenement);
        log.info("Événement ID {} supprimé par client ID {}", evenementId, client.getId());
    }

    private EvenementDto mapToDto(Evenement evenement) {
        EvenementDto dto = new EvenementDto();
        dto.setId(evenement.getId());
        dto.setNom(evenement.getNom());
        dto.setDescription(evenement.getDescription());
        dto.setDateEvenement(evenement.getDateEvenement());
        dto.setLieu(evenement.getLieu());
        dto.setDateCreationEnregistrement(evenement.getDateCreationEnregistrement());
        if (evenement.getClient() != null) {
            dto.setClientId(evenement.getClient().getId());
        }
        // Si vous voulez mapper les invitations ici, vous aurez besoin d'un InvitationDto et d'un mapper
        // if (evenement.getInvitations() != null) {
        //    dto.setInvitations(evenement.getInvitations().stream()
        //        .map(invitationMapper::mapToDto).collect(Collectors.toList()));
        // }
        return dto;
    }
}