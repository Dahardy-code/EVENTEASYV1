package com.eventeasyv1.service;

import com.eventeasyv1.dao.DisponibiliteRepository;
import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.dto.input.DisponibiliteCreateDto;
import com.eventeasyv1.entities.Disponibilite;
import com.eventeasyv1.entities.Prestataire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;

    @Transactional
    public Disponibilite createDisponibilite(DisponibiliteCreateDto dto) {
        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setDateDispo(dto.getDebut().toLocalDate());
        disponibilite.setHeureDebut(dto.getDebut().toLocalTime());
        disponibilite.setHeureFin(dto.getFin().toLocalTime());
        // Assuming there's a method to find Prestataire by ID
        disponibilite.setPrestataire(findPrestataireById(dto.getPrestataireId()));
        return disponibiliteRepository.save(disponibilite);
    }

    public List<Disponibilite> findAll() {
        return disponibiliteRepository.findAll();
    }

    // Add method to find Prestataire by ID (not shown in the snippet)
    private Prestataire findPrestataireById(Long id) {
        // Implementation to find and return a Prestataire by its ID
        return new Prestataire(); // Placeholder
    }

    public List<DisponibiliteDto> getPublicDisponibilites(Long prestataireId) {
        return disponibiliteRepository.findByPrestataireId(prestataireId)
                .stream()
                .map(disponibilite -> new DisponibiliteDto(
                        disponibilite.getId(),
                        disponibilite.getDateDispo(),
                        disponibilite.getHeureDebut(),
                        disponibilite.getHeureFin()
                ))
                .collect(Collectors.toList());
    }
}