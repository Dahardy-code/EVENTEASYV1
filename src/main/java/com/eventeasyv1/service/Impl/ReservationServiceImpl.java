package com.eventeasyv1.service.Impl; // Assurez-vous que le dossier physique est bien 'impl'

import com.eventeasyv1.dao.*; // Importer tous les DAOs nécessaires
import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.dto.input.ReservationCreateDto;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Disponibilite;
import com.eventeasyv1.entities.Prestataire; // Importer Prestataire
import com.eventeasyv1.entities.Reservation;
import com.eventeasyv1.entities.Service; // Importer Service explicitement si pas via *
import com.eventeasyv1.entities.enums.StatutReservation;
import com.eventeasyv1.exception.BadRequestException; // Assurez-vous que l'import est correct
import com.eventeasyv1.exception.ResourceNotFoundException; // Assurez-vous que l'import est correct
import com.eventeasyv1.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component; // Ou @Service, @Component est OK aussi
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component("reservationService") // Nom explicite pour le bean, @Service est aussi bien
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final PrestataireRepository prestataireRepository; // Ajouté pour getReservationByIdForUser

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ClientRepository clientRepository,
                                  ServiceRepository serviceRepository,
                                  DisponibiliteRepository disponibiliteRepository,
                                  PrestataireRepository prestataireRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.disponibiliteRepository = disponibiliteRepository;
        this.prestataireRepository = prestataireRepository;
    }

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationCreateDto reservationCreateDto, Authentication authentication) {
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail) // Assurez-vous que cette méthode existe dans ClientRepository
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));

        // Utiliser le nom de classe complet pour éviter l'ambiguïté avec org.springframework.stereotype.Service
        com.eventeasyv1.entities.Service service = serviceRepository.findById(reservationCreateDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", reservationCreateDto.getServiceId()));

        // --- Vérification de disponibilité (Simplifiée) ---
        List<Disponibilite> disponibilites = disponibiliteRepository.findByPrestataireIdAndDateDebutLessThanAndDateFinGreaterThan(
                service.getPrestataire().getId(),
                reservationCreateDto.getDateReservation(),
                reservationCreateDto.getDateReservation().minusHours(1) // Exemple, à adapter
        );

        if (disponibilites.isEmpty()) {
            log.warn("Tentative de réservation pour service ID {} par client ID {} à une date non disponible: {}",
                    service.getId(), client.getId(), reservationCreateDto.getDateReservation());
            throw new BadRequestException("Le prestataire n'est pas disponible pour la date et heure sélectionnées pour ce service.");
        }
        log.info("Disponibilité confirmée pour le service ID {} à la date {}", service.getId(), reservationCreateDto.getDateReservation());

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setService(service);
        reservation.setDateReservation(reservationCreateDto.getDateReservation());
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        // Assurez-vous que service.getPrix() retourne un type compatible avec BigDecimal.valueOf()
        // ou que vous castez/convertissez correctement. Si getPrix() retourne double:
        reservation.setPrixFinal(service.getPrix());
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Réservation ID {} créée pour client ID {} et service ID {}",
                savedReservation.getId(), client.getId(), service.getId());
        return mapToDto(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto getReservationByIdForUser(Long reservationId, Authentication authentication) {
        String userEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(role -> role.equals("ROLE_ADMIN"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        if (isAdmin) {
            return mapToDto(reservation);
        }

        if (reservation.getClient() != null && reservation.getClient().getEmail().equals(userEmail)) {
            return mapToDto(reservation);
        }

        if (reservation.getService() != null && reservation.getService().getPrestataire() != null &&
                reservation.getService().getPrestataire().getEmail().equals(userEmail)) {
            return mapToDto(reservation);
        }

        log.warn("Tentative d'accès non autorisé à la réservation ID {} par l'utilisateur {}", reservationId, userEmail);
        throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à cette réservation.");
    }

    // --- Helper de Mapping ---
    // Rendue publique pour pouvoir être appelée par ClientServiceImpl et PrestataireServiceImpl
    // Alternative: créer une classe de mapping dédiée.
    public ReservationDto mapToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setStatut(reservation.getStatut());
        dto.setPrixFinal(reservation.getPrixFinal());
        dto.setDateCreation(reservation.getDateCreation());

        Client client = reservation.getClient();
        if (client != null) {
            dto.setClientId(client.getId());
            // Assurez-vous que Client a getPrenom() et getNom()
            dto.setClientNomComplet(client.getPrenom() + " " + client.getNom());
        }

        com.eventeasyv1.entities.Service service = reservation.getService();
        if (service != null) {
            dto.setServiceId(service.getId());
            dto.setServiceNom(service.getTitre()); // Assurez-vous que Service a getNom() ou getTitre()

            Prestataire prestataire = service.getPrestataire();
            if (prestataire != null) {
                dto.setPrestataireId(prestataire.getId());
                // Assurez-vous que Prestataire a getNomEntreprise()
                dto.setPrestataireNomEntreprise(prestataire.getNomEntreprise());
            }
        }
        return dto;
    }
}