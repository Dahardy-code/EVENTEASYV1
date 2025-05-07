package com.eventeasyv1.controller;

import com.eventeasyv1.dto.AdminStatsDto; // Utiliser le DTO Admin
import com.eventeasyv1.dto.PrestataireStatsDto; // Utiliser le DTO Prestataire
import com.eventeasyv1.service.StatistiqueService;
import org.slf4j.Logger; // Importer Logger
import org.slf4j.LoggerFactory; // Importer LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// Pas besoin de Map ici

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    private static final Logger log = LoggerFactory.getLogger(StatistiqueController.class); // Logger

    @Autowired // S'assurer que le service est injecté
    private StatistiqueService statistiqueService;

    // Endpoint pour les stats Admin
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsDto> getAdminDashboardStats() {
        log.info("Requête reçue pour les statistiques admin.");
        try {
            AdminStatsDto stats = statistiqueService.getAdminStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des statistiques admin.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur calcul stats admin.", e);
        }
    }

    // Endpoint pour les stats Prestataire
    @GetMapping("/prestataire/me")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<PrestataireStatsDto> getMyPrestataireStats() {
        log.info("Requête reçue pour les statistiques du prestataire connecté.");
        try {
            PrestataireStatsDto stats = statistiqueService.getMyPrestataireStats();
            return ResponseEntity.ok(stats);
        } catch (IllegalStateException e) { // Capturer si non authentifié correctement
            log.warn("Tentative d'accès aux stats prestataire non authentifié.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des statistiques prestataire.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur calcul stats prestataire.", e);
        }
    }
}