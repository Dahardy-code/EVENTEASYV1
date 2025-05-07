package com.eventeasyv1.controller;

// Ce contrôleur est optionnel. La gestion (ajout/suppression) des disponibilités
// est déjà incluse dans PrestataireController (/api/prestataires/me/disponibilites).
// On pourrait l'utiliser pour des endpoints publics si nécessaire.

import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.service.DisponibiliteService; // Créez ce service si logique publique nécessaire
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilites") // Exemple de chemin public
public class DisponibiliteController {

    @Autowired(required = false) // Optionnel car peut ne pas exister au début
    private DisponibiliteService disponibiliteService;

    // Endpoint PUBLIC (permitAll dans SecurityConfig) pour voir les dispos d'un prestataire
    @GetMapping("/prestataire/{prestataireId}")
    public ResponseEntity<List<DisponibiliteDto>> getDisponibilitesForPrestataire(@PathVariable Long prestataireId) {
        if (disponibiliteService == null) {
            return ResponseEntity.ok(Collections.emptyList()); // Service non implémenté
        }
        // TODO: Implémenter la logique dans DisponibiliteService
        List<DisponibiliteDto> disponibilites = disponibiliteService.getPublicDisponibilites(prestataireId); // Exemple
        return ResponseEntity.ok(disponibilites);
    }
}