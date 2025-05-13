package com.eventeasyv1.controller;

import com.eventeasyv1.dto.StatistiqueDto;
import com.eventeasyv1.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    // --- Endpoints pour statistiques calculées à la volée (Exemples) ---
    @GetMapping("/admin/total-utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getTotalUtilisateurs() {
        return ResponseEntity.ok(Map.of("totalUtilisateurs", statistiqueService.getTotalUtilisateurs()));
    }

    @GetMapping("/admin/total-clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getTotalClients() {
        return ResponseEntity.ok(Map.of("totalClients", statistiqueService.getTotalClients()));
    }

    @GetMapping("/admin/total-prestataires")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getTotalPrestataires() {
        return ResponseEntity.ok(Map.of("totalPrestataires", statistiqueService.getTotalPrestataires()));
    }

    @GetMapping("/admin/reservations-par-statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getReservationsParStatut() {
        return ResponseEntity.ok(statistiqueService.getReservationsParStatut());
    }

    // --- Endpoints pour statistiques stockées (Exemples) ---
    @GetMapping("/admin/type/{typeStatistique}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StatistiqueDto>> getStatistiquesStockeesParType(
            @PathVariable String typeStatistique,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<StatistiqueDto> stats = statistiqueService.getStatistiquesParTypeEtPeriode(typeStatistique, dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    // Endpoint pour déclencher manuellement la génération de stats (pourrait être un POST)
    @PostMapping("/admin/generer-quotidiennes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> genererStatsQuotidiennes() {
        statistiqueService.genererStatistiquesQuotidiennes();
        return ResponseEntity.ok("Génération des statistiques quotidiennes lancée.");
    }
}