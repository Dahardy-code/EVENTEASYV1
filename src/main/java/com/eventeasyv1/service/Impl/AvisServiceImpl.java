package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.*; // Importer les DAOs nécessaires
import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;
import com.eventeasyv1.entities.*; // Importer les Entités
import com.eventeasyv1.exception.BadRequestException;
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.AvisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AvisServiceImpl implements AvisService {

    private static final Logger log = LoggerFactory.getLogger(AvisServiceImpl.class);

    private final AvisRepository avisRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository; // Pour vérifier si le client a réservé
    private final PrestataireRepository prestataireRepository;

    @Autowired
    public AvisServiceImpl(AvisRepository avisRepository,
                           ClientRepository clientRepository,
                           ServiceRepository serviceRepository,
                           ReservationRepository reservationRepository,
                           PrestataireRepository prestataireRepository) {
        this.avisRepository = avisRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;
        this.prestataireRepository = prestataireRepository;
    }

    @Override
    @Transactional
    public AvisDto addAvisToService(Long serviceId, AvisCreateDto avisCreateDto, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        com.eventeasyv1.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        // Vérification 1 : Le client n'a-t-il pas déjà laissé un avis pour ce service ?
        Optional<Avis> existingAvis = avisRepository.findByClientIdAndServiceId(client.getId(), serviceId);
        if (existingAvis.isPresent()) {
            log.warn("Le client ID {} a déjà laissé un avis pour le service ID {}", client.getId(), serviceId);
            throw new BadRequestException("Vous avez déjà laissé un avis pour ce service.");
        }

        // Vérification 2 (Optionnelle mais recommandée) : Le client a-t-il réservé et utilisé ce service ?
        // Cette logique peut être complexe (vérifier statut de réservation "TERMINEE", etc.)
        // Pour l'instant, on simplifie, mais c'est un point d'amélioration.
        /*
        boolean hasUsedService = reservationRepository.findByClientIdOrderByDateReservationDesc(client.getId())
                                   .stream()
                                   .anyMatch(r -> r.getService().getId().equals(serviceId) &&
                                                  r.getStatut() == StatutReservation.TERMINEE);
        if (!hasUsedService) {
            log.warn("Le client ID {} essaie de laisser un avis pour le service ID {} sans l'avoir utilisé.", client.getId(), serviceId);
            throw new BadRequestException("Vous devez avoir utilisé ce service pour laisser un avis.");
        }
        */

        Avis avis = new Avis();
        avis.setCommentaire(avisCreateDto.getCommentaire());
        avis.setNote(avisCreateDto.getNote());
        avis.setClient(client);
        avis.setService(service);

        Avis savedAvis = avisRepository.save(avis);
        log.info("Avis ID {} créé par client ID {} pour service ID {}", savedAvis.getId(), client.getId(), serviceId);
        return mapToDto(savedAvis);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvisDto> getAvisForService(Long serviceId, Pageable pageable) {
        // Vérifier que le service existe
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        Page<Avis> avisPage = avisRepository.findByServiceIdOrderByDateAvisDesc(serviceId, pageable);
        log.debug("Récupération des avis pour le service ID {} - Page {}/{}", serviceId, avisPage.getNumber(), avisPage.getTotalPages());
        return avisPage.map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvisDto> getAvisForMyServices(Authentication authentication, Pageable pageable) {
        String prestataireEmail = authentication.getName();
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));

        Page<Avis> avisPage = avisRepository.findByServicePrestataireIdOrderByDateAvisDesc(prestataire.getId(), pageable);
        log.debug("Récupération des avis pour les services du prestataire ID {} - Page {}/{}",
                prestataire.getId(), avisPage.getNumber(), avisPage.getTotalPages());
        return avisPage.map(this::mapToDto);
    }


    private AvisDto mapToDto(Avis avis) {
        AvisDto dto = new AvisDto();
        dto.setId(avis.getId());
        dto.setCommentaire(avis.getCommentaire());
        dto.setNote(avis.getNote());
        dto.setDateAvis(avis.getDateAvis());

        if (avis.getClient() != null) {
            dto.setClientId(avis.getClient().getId());
            dto.setClientNomComplet(avis.getClient().getPrenom() + " " + avis.getClient().getNom());
        }
        if (avis.getService() != null) {
            dto.setServiceId(avis.getService().getId());
            dto.setServiceNom(avis.getService().getTitre()); // ou getTitre
        }
        return dto;
    }
}
