package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Statistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {

    // Trouver une statistique par son type et une date de calcul exacte
    Optional<Statistique> findByTypeStatistiqueAndDateCalcul(String typeStatistique, LocalDate dateCalcul);

    // Trouver des statistiques par type pour une période donnée
    List<Statistique> findByTypeStatistiqueAndDateCalculBetweenOrderByDateCalculAsc(
            String typeStatistique,
            LocalDate dateDebutPeriode,
            LocalDate dateFinPeriode
    );

    // Trouver toutes les statistiques pour une date de calcul donnée
    List<Statistique> findByDateCalcul(LocalDate dateCalcul);

    // Trouver les statistiques dont la période de référence (periodeDebut/periodeFin)
    // chevauche une date donnée.
    List<Statistique> findByTypeStatistiqueAndPeriodeDebutLessThanEqualAndPeriodeFinGreaterThanEqual(
            String typeStatistique,
            LocalDate dateReference,
            LocalDate dateReference2 // Même date pour un point dans le temps
    );
}