package com.eventeasyv1.controller;

import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;
import com.eventeasyv1.service.AvisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class AvisController {

    @Autowired
    private AvisService avisService;

    @GetMapping("/{serviceId}/avis")
    public ResponseEntity<List<AvisDto>> getAvisForService(@PathVariable Long serviceId) {
        List<AvisDto> avisList = avisService.getAvisByServiceId(serviceId);
        return ResponseEntity.ok(avisList);
    }

    @PostMapping("/{serviceId}/avis")
    public ResponseEntity<AvisDto> laisserAvis(@PathVariable Long serviceId, @RequestBody AvisCreateDto avisDto) {
        AvisDto createdAvis = avisService.createAvis(serviceId, avisDto);
        return ResponseEntity.status(201).body(createdAvis);
    }
}