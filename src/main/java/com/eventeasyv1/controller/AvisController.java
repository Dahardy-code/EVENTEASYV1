package com.eventeasyv1.controller;

import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;
import com.eventeasyv1.service.AvisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Chemin de base
public class AvisController {

    @Autowired
    private AvisService avisService;

    // Endpoint pour un CLIENT pour ajouter un avis à un service
    @PostMapping("/services/{serviceId}/avis")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AvisDto> addAvisToService(
            @PathVariable Long serviceId,
            @Valid @RequestBody AvisCreateDto avisCreateDto,
            Authentication authentication) {
        AvisDto newAvis = avisService.addAvisToService(serviceId, avisCreateDto, authentication);
        return new ResponseEntity<>(newAvis, HttpStatus.CREATED);
    }

    // Endpoint PUBLIC pour voir les avis d'un service (avec pagination)
    @GetMapping("/services/{serviceId}/avis")
    public ResponseEntity<Page<AvisDto>> getAvisForService(
            @PathVariable Long serviceId,
            @PageableDefault(size = 10, sort = "dateAvis,desc") Pageable pageable) { // Valeurs par défaut pour la pagination
        Page<AvisDto> avisPage = avisService.getAvisForService(serviceId, pageable);
        return ResponseEntity.ok(avisPage);
    }

    // Endpoint pour un PRESTATAIRE pour voir les avis sur ses services (avec pagination)
    @GetMapping("/prestataires/me/avis")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Page<AvisDto>> getAvisForMyServices(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "dateAvis,desc") Pageable pageable) {
        Page<AvisDto> avisPage = avisService.getAvisForMyServices(authentication, pageable);
        return ResponseEntity.ok(avisPage);
    }

    // Ajoutez d'autres endpoints si nécessaire (DELETE, PUT pour un avis)
}