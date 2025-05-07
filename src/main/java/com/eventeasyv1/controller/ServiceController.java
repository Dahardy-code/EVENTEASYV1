package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.service.ServiceService; // Créez ce service pour la logique publique
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Pour la pagination
import org.springframework.data.domain.Pageable; // Pour la pagination
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/services") // Endpoints pour la consultation des services
public class ServiceController {

    @Autowired(required = false)
    private ServiceService serviceService; // Service dédié à la logique publique/recherche

    // Endpoint PUBLIC pour lister/rechercher des services avec pagination/filtres
    @GetMapping
    public ResponseEntity<Page<ServiceDto>> findServices(
            @RequestParam(required = false) String query, // Recherche textuelle
            @RequestParam(required = false) String categorie, // Filtre catégorie
            Pageable pageable // Spring Data gère la pagination (ex: ?page=0&size=10&sort=titre,asc)
    ) {
        if (serviceService == null) {
            return ResponseEntity.ok(Page.empty(pageable));
        }
        try {
            // TODO: Implémenter la recherche/filtrage dans ServiceService
            Page<ServiceDto> servicePage = serviceService.findAvailableServices(query, categorie, pageable); // Exemple
            return ResponseEntity.ok(servicePage);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur recherche services.", e);
        }
    }

    // Endpoint PUBLIC pour voir le détail d'un service
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        if (serviceService == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service non trouvé.");
        }
        try {
            // TODO: Implémenter serviceService.findServiceDtoById(id);
            ServiceDto service = serviceService.findServiceDtoById(id); // Exemple
            return ResponseEntity.ok(service);
        } catch (Exception e) { // Adaptez l'exception si service non trouvé
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service non trouvé.", e);
        }
    }
}