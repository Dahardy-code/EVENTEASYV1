package com.eventeasyv1.service.impl;

import com.eventeasyv1.dao.*; // Importer tous les repositories nécessaires
import com.eventeasyv1.dto.AdminStatsDto;
import com.eventeasyv1.dto.PrestataireStatsDto;
import com.eventeasyv1.entities.Prestataire; // Importer l'entité
import com.eventeasyv1.service.StatistiqueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Pour les calculs de revenu/moyenne

@Service
@Transactional(readOnly = true) // La plupart des méthodes ici seront en lecture seule
public class StatistiqueServiceImpl implements StatistiqueService {

    private static final Logger log = LoggerFactory.getLogger(StatistiqueServiceImpl.class);

    // Injecter TOUS les repositories nécessaires pour calculer les statistiques
    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private PrestataireRepository prestataireRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private AvisRepository avisRepository; // Pour la note moyenne
    @Autowired private PaiementRepository paiementRepository; // Pour le revenu

    @Override
    public AdminStatsDto getAdminStats() {
        log.info("Calcul des statistiques administrateur...");
        try {
            // Utiliser les méthodes count() des repositories
            long totalUsers = utilisateurRepository.count();
            long totalClients = clientRepository.count();
            long totalPrestataires = prestataireRepository.count();
            long totalReservations = reservationRepository.count();
            long totalServices = serviceRepository.count();

            // TODO: Implémenter des requêtes plus complexes si nécessaire
            // (ex: revenu total, services populaires)

            log.info("Statistiques admin calculées.");
            return new AdminStatsDto(
                    totalUsers,
                    totalClients,
                    totalPrestataires,
                    totalReservations,
                    totalServices
                    // autres stats initialisées à null ou 0
            );
        } catch (Exception e) {
            log.error("Erreur lors du calcul des statistiques admin", e);
            // Retourner un DTO vide ou avec des valeurs d'erreur, ou lever une exception
            return new AdminStatsDto(); // Retourne un DTO vide en cas d'erreur
        }
    }

    @Override
    public PrestataireStatsDto getMyPrestataireStats() {
        log.info("Calcul des statistiques pour le prestataire connecté...");

        // 1. Récupérer l'ID du prestataire connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("Aucun prestataire authentifié trouvé.");
        }
        String userEmail = authentication.getName();
        Prestataire prestataire = prestataireRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Prestataire non trouvé: " + userEmail));
        Long prestataireId = prestataire.getId();

        log.debug("Calcul des stats pour Prestataire ID: {}", prestataireId);

        try {
            // 2. Calculer les statistiques spécifiques
            long totalReservations = reservationRepository.countByServicePrestataireId(prestataireId); // Méthode à ajouter au Repo

            // Exemple pour compter les réservations complétées (à adapter selon votre logique de statut)
            long completedReservations = reservationRepository.countByServicePrestataireIdAndStatut(prestataireId, "TERMINEE"); // Méthode à ajouter

            // Exemple pour les réservations en attente
            long pendingReservations = reservationRepository.countByServicePrestataireIdAndStatut(prestataireId, "EN_ATTENTE"); // Méthode à ajouter

            // Exemple pour la note moyenne (nécessite une requête JPQL ou Native Query)
            Double averageRating = avisRepository.findAverageRatingByPrestataireId(prestataireId).orElse(0.0); // Méthode à ajouter

            // Exemple pour le revenu total (nécessite une requête JPQL ou Native Query)
            BigDecimal totalRevenue = paiementRepository.findTotalRevenueByPrestataireId(prestataireId).orElse(BigDecimal.ZERO); // Méthode à ajouter

            log.info("Statistiques calculées pour Prestataire ID: {}", prestataireId);

            return new PrestataireStatsDto(
                    totalReservations,
                    completedReservations,
                    pendingReservations,
                    averageRating,
                    totalRevenue
            );
        } catch (Exception e) {
            log.error("Erreur lors du calcul des statistiques pour Prestataire ID {}: {}", prestataireId, e.getMessage(), e);
            return new PrestataireStatsDto(); // Retourne un DTO vide en cas d'erreur
        }
    }

    // --- Méthodes de Repository à Ajouter ---
    // Dans ReservationRepository:
    // long countByServicePrestataireId(Long prestataireId);
    // long countByServicePrestataireIdAndStatut(Long prestataireId, String statut);

    // Dans AvisRepository (exemple avec JPQL):
    // @Query("SELECT AVG(a.note) FROM Avis a WHERE a.service.prestataire.id = :prestataireId")
    // Optional<Double> findAverageRatingByPrestataireId(@Param("prestataireId") Long prestataireId);

    // Dans PaiementRepository (exemple avec JPQL):
    // @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.reservation.service.prestataire.id = :prestataireId")
    // Optional<BigDecimal> findTotalRevenueByPrestataireId(@Param("prestataireId") Long prestataireId);

}