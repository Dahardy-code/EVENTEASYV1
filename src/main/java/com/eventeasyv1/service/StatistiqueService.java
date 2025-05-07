package com.eventeasyv1.service;

import com.eventeasyv1.dto.AdminStatsDto;
import com.eventeasyv1.dto.PrestataireStatsDto;
// Importez d'autres DTOs si nécessaire

public interface StatistiqueService {

    /**
     * Récupère les statistiques globales pour le tableau de bord administrateur.
     * @return Un DTO contenant les statistiques agrégées.
     */
    AdminStatsDto getAdminStats();

    /**
     * Récupère les statistiques spécifiques pour le prestataire actuellement connecté.
     * @return Un DTO contenant les statistiques du prestataire.
     */
    PrestataireStatsDto getMyPrestataireStats();

    // Ajoutez d'autres méthodes de statistiques si nécessaire
}