package com.eventeasyv1.service;

import com.eventeasyv1.dto.StatistiqueDto; // Assurez-vous que cet import est correct

import java.time.LocalDate;
import java.util.List;
import java.util.Map; // Pour retourner des stats calculées à la volée

public interface StatistiqueService {

    // Exemple de statistiques calculées à la volée
    long getTotalUtilisateurs();
    long getTotalClients();
    long getTotalPrestataires();
    long getTotalServices();
    long getTotalReservations();
    Map<String, Long> getReservationsParStatut();
    // BigDecimal getRevenuTotal(); // Nécessiterait l'entité Paiement

    // Pour les statistiques stockées
    StatistiqueDto saveStatistique(String type, String valeur, LocalDate dateCalcul, LocalDate periodeDebut, LocalDate periodeFin, String details);
    List<StatistiqueDto> getStatistiquesParTypeEtPeriode(String typeStatistique, LocalDate debut, LocalDate fin);
    List<StatistiqueDto> getStatistiquesParTypePourDate(String typeStatistique, LocalDate date);

    // Méthode pour générer et sauvegarder des statistiques périodiques (ex: via un scheduler)
    void genererStatistiquesQuotidiennes(); // Ou mensuelles, etc.
}
