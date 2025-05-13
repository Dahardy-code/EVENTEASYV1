package com.eventeasyv1.service.Impl; // Package pour les implémentations

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.ReservationRepository; // <-- AJOUTÉ
import com.eventeasyv1.dao.UtilisateurRepository;
import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.dto.ReservationDto; // <-- AJOUTÉ
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Reservation; // <-- AJOUTÉ
import com.eventeasyv1.entities.Utilisateur;
import com.eventeasyv1.exception.ResourceNotFoundException; // Assurez-vous que cette exception existe et est appropriée
import com.eventeasyv1.service.ClientService;
// Pour le mapping de ReservationDto, nous allons injecter ReservationServiceImpl
// Si vous n'avez pas ReservationServiceImpl ou préférez ne pas l'injecter pour ça,
// vous devrez dupliquer la logique de mapToDto(Reservation reservation) ici.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Plus approprié pour un utilisateur non trouvé
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indique que c'est un bean de service géré par Spring
public class ClientServiceImpl implements ClientService { // Implémente l'interface ClientService

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository; // Spécifique pour les opérations sur Client
    private final UtilisateurRepository utilisateurRepository; // Pour récupérer l'Utilisateur de base
    private final ReservationRepository reservationRepository; // Pour les réservations
    private final ReservationServiceImpl reservationServiceMapper; // Pour mapper Reservation en ReservationDto

    // Injection par constructeur (meilleure pratique)
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             UtilisateurRepository utilisateurRepository,
                             ReservationRepository reservationRepository,
                             ReservationServiceImpl reservationServiceMapper) {
        this.clientRepository = clientRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.reservationRepository = reservationRepository;
        this.reservationServiceMapper = reservationServiceMapper;
    }

    @Override
    @Transactional(readOnly = true) // Opération de lecture seule
    public ClientDto getCurrentClientDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("getCurrentClientDetails appelé sans utilisateur authentifié valide.");
            // Vous pourriez lancer une exception personnalisée pour "Non authentifié" qui se traduit en 401
            throw new IllegalStateException("Aucun utilisateur authentifié valide trouvé. Veuillez vous connecter.");
        }
        String userEmail = authentication.getName();
        log.debug("Récupération des détails pour le client avec l'email: {}", userEmail);

        // D'abord, récupérer l'entité Utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("Utilisateur authentifié (email: {}) introuvable dans la base de données. Cela ne devrait pas arriver si le token est valide.", userEmail);
                    // UsernameNotFoundException est approprié si le service UserDetails ne le trouve pas
                    // Mais ici, l'utilisateur est déjà authentifié, donc c'est une incohérence de données
                    return new ResourceNotFoundException("Utilisateur", "email", userEmail);
                });

        // Ensuite, vérifier si c'est bien un Client et caster
        if (!(utilisateur instanceof Client)) {
            log.warn("L'utilisateur authentifié (email: {}) n'est pas un Client. Type réel: {}", userEmail, utilisateur.getClass().getSimpleName());
            // Si l'utilisateur authentifié n'a pas le rôle attendu, c'est un problème d'accès
            // Une exception personnalisée mappée à 403 Forbidden serait plus appropriée qu'une 404.
            // Pour l'instant, ResourceNotFoundException peut servir, mais pensez à la sémantique.
            throw new ResourceNotFoundException("Client", "email (type incorrect)", userEmail);
        }

        Client client = (Client) utilisateur;
        log.info("Détails du client trouvés pour l'email: {}", userEmail);
        return mapToClientDto(client); // Convertir l'entité en DTO
    }

    @Override
    @Transactional(readOnly = true) // Opération de lecture seule
    public List<ReservationDto> getMyReservations(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("getMyReservations appelé sans utilisateur authentifié valide.");
            throw new IllegalStateException("Aucun utilisateur authentifié valide trouvé. Veuillez vous connecter.");
        }
        String clientEmail = authentication.getName();
        log.debug("Récupération des réservations pour le client avec email : {}", clientEmail);

        // Récupérer l'entité Client pour obtenir son ID
        Client client = clientRepository.findByEmail(clientEmail) // Utiliser clientRepository est plus direct ici
                .orElseThrow(() -> {
                    log.warn("Client non trouvé lors de la récupération des réservations pour l'email : {}. Cela ne devrait pas arriver pour un utilisateur authentifié.", clientEmail);
                    return new ResourceNotFoundException("Client", "email", clientEmail);
                });

        List<Reservation> reservations = reservationRepository.findByClientIdOrderByDateReservationDesc(client.getId());
        log.info("{} réservations trouvées pour le client ID {}", reservations.size(), client.getId());

        // Mapper la liste d'entités Reservation en une liste de ReservationDto
        // en utilisant la méthode de mapping de ReservationServiceImpl
        return reservations.stream()
                .map(reservationServiceMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mappe une entité Client vers un ClientDto.
     *
     * @param client L'entité Client.
     * @return Le ClientDto correspondant.
     */
    private ClientDto mapToClientDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        // Le rôle est implicitement "CLIENT" pour ce DTO
        dto.setRole("CLIENT"); // On peut le fixer ici
        dto.setDateInscription(client.getDateInscription());
        // Ajoutez d'autres champs spécifiques à ClientDto si vous en avez
        return dto;
    }
}