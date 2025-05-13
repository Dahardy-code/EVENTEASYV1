package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.*; // Importer tous les repositories nécessaires
import com.eventeasyv1.dto.StatistiqueDto;
import com.eventeasyv1.entities.Reservation; // Pour compter par statut
import com.eventeasyv1.entities.Statistique;
import com.eventeasyv1.entities.enums.StatutReservation; // Pour compter par statut
import com.eventeasyv1.service.StatistiqueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatistiqueServiceImpl implements StatistiqueService {

    private static final Logger log = LoggerFactory.getLogger(StatistiqueServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;
    private final ClientRepository clientRepository;
    private final PrestataireRepository prestataireRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;
    private final StatistiqueRepository statistiqueRepository;
    // private final PaiementRepository paiementRepository; // Si vous calculez des revenus

    @Autowired
    public StatistiqueServiceImpl(UtilisateurRepository utilisateurRepository,
                                  ClientRepository clientRepository,
                                  PrestataireRepository prestataireRepository,
                                  ServiceRepository serviceRepository,
                                  ReservationRepository reservationRepository,
                                  StatistiqueRepository statistiqueRepository
            /*, PaiementRepository paiementRepository */) {
        this.utilisateurRepository = utilisateurRepository;
        this.clientRepository = clientRepository;
        this.prestataireRepository = prestataireRepository;
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;
        this.statistiqueRepository = statistiqueRepository;
        // this.paiementRepository = paiementRepository;
    }

    // --- Exemples de Statistiques Calculées à la Volée ---
    @Override
    @Transactional(readOnly = true)
    public long getTotalUtilisateurs() {
        return utilisateurRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalClients() {
        return clientRepository.count(); // Suppose que Client est une entité distincte ou identifiable
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPrestataires() {
        return prestataireRepository.count(); // Suppose que Prestataire est une entité distincte
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalServices() {
        return serviceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalReservations() {
        return reservationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getReservationsParStatut() {
        return reservationRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getStatut().name(), Collectors.counting()));
    }

    // --- Gestion des Statistiques Stockées ---
    @Override
    @Transactional
    public StatistiqueDto saveStatistique(String type, String valeur, LocalDate dateCalcul,
                                          LocalDate periodeDebut, LocalDate periodeFin, String details) {
        Statistique stat = new Statistique();
        stat.setTypeStatistique(type);
        stat.setValeur(valeur);
        stat.setDateCalcul(dateCalcul);
        stat.setPeriodeDebut(periodeDebut);
        stat.setPeriodeFin(periodeFin);
        stat.setDetails(details);
        Statistique savedStat = statistiqueRepository.save(stat);
        log.info("Statistique sauvegardée: ID {}, Type {}", savedStat.getId(), savedStat.getTypeStatistique());
        return mapToDto(savedStat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatistiqueDto> getStatistiquesParTypeEtPeriode(String typeStatistique, LocalDate debut, LocalDate fin) {
        List<Statistique> stats = statistiqueRepository.findByTypeStatistiqueAndDateCalculBetweenOrderByDateCalculAsc(typeStatistique, debut, fin);
        return stats.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatistiqueDto> getStatistiquesParTypePourDate(String typeStatistique, LocalDate date) {
        List<Statistique> stats = statistiqueRepository.findByTypeStatistiqueAndDateCalculBetweenOrderByDateCalculAsc(typeStatistique, date, date);
        return stats.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void genererStatistiquesQuotidiennes() {
        LocalDate today = LocalDate.now();
        log.info("Génération des statistiques quotidiennes pour le {}", today);

        // Exemple: Nombre total d'utilisateurs
        long totalUsers = getTotalUtilisateurs();
        saveStatistique("TOTAL_UTILISATEURS", String.valueOf(totalUsers), today, today, today, "Nombre total d'utilisateurs enregistrés.");

        // Exemple: Nombre de nouvelles inscriptions aujourd'hui
        // Pour cela, il faudrait un champ 'dateCreation' ou 'dateInscription' sur Utilisateur
        // long nouveauxUtilisateurs = utilisateurRepository.countByDateCreation(today); // Méthode à créer
        // saveStatistique("NOUVEAUX_UTILISATEURS_JOUR", String.valueOf(nouveauxUtilisateurs), today, today, today, "Nouveaux utilisateurs inscrits aujourd'hui.");

        // Exemple: Nombre de réservations créées aujourd'hui
        // Nécessite un champ dateCreation sur Reservation et une méthode de repo
        // long nouvellesReservations = reservationRepository.countByDateCreation(today); // Méthode à créer
        // saveStatistique("NOUVELLES_RESERVATIONS_JOUR", String.valueOf(nouvellesReservations), today, today, today, "Nouvelles réservations créées aujourd'hui.");

        log.info("Statistiques quotidiennes générées.");
    }


    private StatistiqueDto mapToDto(Statistique statistique) {
        StatistiqueDto dto = new StatistiqueDto();
        dto.setId(statistique.getId());
        dto.setTypeStatistique(statistique.getTypeStatistique());
        dto.setValeur(statistique.getValeur());
        dto.setPeriodeDebut(statistique.getPeriodeDebut());
        dto.setPeriodeFin(statistique.getPeriodeFin());
        dto.setDateCalcul(statistique.getDateCalcul());
        dto.setDetails(statistique.getDetails());
        return dto;
    }
}
