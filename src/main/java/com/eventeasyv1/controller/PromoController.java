package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PromoDto; // Créez ce DTO
import com.eventeasyv1.service.PromoService; // Créez ce Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/promos")
public class PromoController {

    @Autowired(required = false) // Optionnel
    private PromoService promoService;

    // Endpoint public pour voir les promos actives
    @GetMapping("/actives")
    public ResponseEntity<List<PromoDto>> getActivePromos() {
        if (promoService == null) return ResponseEntity.ok(Collections.emptyList());
        // TODO: Implémenter promoService.getActivePromos();
        return ResponseEntity.ok(Collections.emptyList()); // Placeholder
    }

    // Endpoint admin pour créer une promo
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoDto> createPromo(/*@Valid @RequestBody PromoCreateDto promoDto*/) {
        if (promoService == null) return ResponseEntity.status(501).build();
        // TODO: Implémenter promoService.createPromo(promoDto);
        return ResponseEntity.status(201).body(new PromoDto()); // Placeholder
    }

    // Ajoutez d'autres endpoints admin (update, delete)
}