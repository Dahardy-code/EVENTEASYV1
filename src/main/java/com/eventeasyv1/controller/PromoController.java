package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PromoDto;
import com.eventeasyv1.dto.input.PromoCreateDto;
import com.eventeasyv1.service.PromoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promos")
public class PromoController {

    @Autowired
    private PromoService promoService;

    // --- Endpoints Admin ---
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoDto> createPromo(@Valid @RequestBody PromoCreateDto promoCreateDto) {
        PromoDto createdPromo = promoService.createPromo(promoCreateDto);
        return new ResponseEntity<>(createdPromo, HttpStatus.CREATED);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PromoDto>> getAllPromosForAdmin() {
        return ResponseEntity.ok(promoService.getAllPromos());
    }

    @PutMapping("/{promoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoDto> updatePromo(@PathVariable Long promoId, @Valid @RequestBody PromoCreateDto promoUpdateDto) {
        return ResponseEntity.ok(promoService.updatePromo(promoId, promoUpdateDto));
    }

    @PatchMapping("/{promoId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoDto> activatePromo(@PathVariable Long promoId) {
        return ResponseEntity.ok(promoService.activatePromo(promoId));
    }

    @PatchMapping("/{promoId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoDto> deactivatePromo(@PathVariable Long promoId) {
        return ResponseEntity.ok(promoService.deactivatePromo(promoId));
    }

    @DeleteMapping("/{promoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePromo(@PathVariable Long promoId) {
        promoService.deletePromo(promoId);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints Publics/Clients ---
    @GetMapping("/validate/{codePromo}")
    public ResponseEntity<PromoDto> validatePromoCode(@PathVariable String codePromo) {
        // Ce service lèvera une exception si le code n'est pas valide/actif
        return ResponseEntity.ok(promoService.validatePromoCode(codePromo));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromoDto>> getActivePublicPromos() {
        return ResponseEntity.ok(promoService.getActivePublicPromos());
    }
}