package com.eventeasyv1.controller;

import com.eventeasyv1.service.RecommendationService; // Créez ce service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired(required = false) // Optionnel
    private RecommendationService recommendationService;

    // Endpoint pour obtenir des recommandations pour le client connecté
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')") // Seulement pour les clients connectés
    public ResponseEntity<List<?>> getRecommendationsForCurrentUser(
            @RequestParam(required = false) String context // Ex: 'homepage', 'event_details'
    ) {
        if (recommendationService == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        // TODO: Implémenter la logique de recommandation dans RecommendationService
        // Récupérer l'ID client depuis SecurityContextHolder
        List<?> recommendations = recommendationService.getRecommendations(/*clientId, context*/); // Placeholder
        return ResponseEntity.ok(recommendations);
    }
}