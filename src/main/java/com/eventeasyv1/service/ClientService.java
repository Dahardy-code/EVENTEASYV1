package com.eventeasyv1.service;

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.UtilisateurRepository; // <-- IMPORTER UtilisateurRepository
import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Utilisateur; // <-- IMPORTER Utilisateur
import org.slf4j.Logger; // Importer Logger
import org.slf4j.LoggerFactory; // Importer LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Importer Authentication
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Pour la lecture DB

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class); // Ajouter Logger

    @Autowired
    private ClientRepository clientRepository; // Garder pour opérations spécifiques Client si besoin

    @Autowired
    private UtilisateurRepository utilisateurRepository; // <-- INJECTER UtilisateurRepository

    /**
     * Récupère les détails du client actuellement connecté.
     *
     * @return ClientDto contenant les informations du client.
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé ou n'est pas un client.
     * @throws IllegalStateException si aucun utilisateur n'est authentifié.
     */
    @Transactional(readOnly = true) // Bonne pratique pour les lectures
    public ClientDto getCurrentClientDetails() {
        // 1. Récupérer l'authentification actuelle
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("getCurrentClientDetails appelé sans utilisateur authentifié.");
            throw new IllegalStateException("Aucun utilisateur authentifié trouvé.");
        }
        String userEmail = authentication.getName(); // Email de l'utilisateur connecté
        log.debug("Récupération des détails pour le client avec l'email: {}", userEmail);


        // 2. Trouver l'Utilisateur par email en utilisant le Repository de base
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userEmail) // <-- UTILISER utilisateurRepository
                .orElseThrow(() -> {
                    // Ceci ne devrait pas arriver si le token est valide et UserDetailsServiceImpl fonctionne
                    log.error("Utilisateur authentifié introuvable dans la DB : {}", userEmail);
                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + userEmail);
                });

        // 3. Vérifier si l'utilisateur trouvé est bien une instance de Client
        if (!(utilisateur instanceof Client)) {
            log.warn("L'utilisateur connecté ({}) n'est pas un Client.", userEmail);
            // Lever une exception spécifique ou UsernameNotFoundException
            throw new UsernameNotFoundException("L'utilisateur connecté n'est pas un Client: " + userEmail);
            // Alternativement, vous pourriez renvoyer une erreur 403 Forbidden via une exception personnalisée
        }

        // 4. Caster en Client et mapper en DTO
        Client client = (Client) utilisateur;
        log.info("Détails du client trouvés pour l'email: {}", userEmail);
        return mapToDto(client); // Convertir l'entité en DTO
    }


    /**
     * Mappe une entité Client vers un ClientDto.
     *
     * @param client L'entité Client.
     * @return Le ClientDto correspondant.
     */
    private ClientDto mapToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        dto.setDateInscription(client.getDateInscription());
        // Ajoutez d'autres champs du DTO si nécessaire
        return dto;
    }

    // Ajoutez d'autres méthodes spécifiques au ClientService ici si nécessaire
    // (par exemple, getMyReservations, updateProfile, etc.)
}