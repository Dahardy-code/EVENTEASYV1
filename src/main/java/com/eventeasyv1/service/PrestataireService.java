package com.eventeasyv1.service;

import com.eventeasyv1.dao.DisponibiliteRepository;
import com.eventeasyv1.dao.PrestataireRepository;
import com.eventeasyv1.dao.ServiceRepository;
import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.dto.PrestataireDto;
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.input.DisponibiliteCreateDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;
import com.eventeasyv1.entities.Disponibilite;
import com.eventeasyv1.entities.Prestataire;
import com.eventeasyv1.entities.Service;
import com.eventeasyv1.exception.BadRequestException;
import com.eventeasyv1.exception.ResourceNotFoundException; // Vous devrez créer cette exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // Import for security check
import org.springframework.stereotype.Component; // Use @Service usually
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Component // Ou @Service pour la sémantique
public class PrestataireService { // Renommez en PrestataireServiceImpl si vous utilisez une interface

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PrestataireRepository prestataireRepository;
    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    // --- Méthodes pour la gestion des Services par le Prestataire ---

    @Transactional
    public ServiceDto addService(ServiceCreateUpdateDto serviceDto, String prestataireEmail) {
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));

        Service service = new Service();
        service.setNom(serviceDto.getNom());
        service.setDescription(serviceDto.getDescription());
        service.setPrix(serviceDto.getPrix());
        service.setPrestataire(prestataire); // Link the service to the prestataire

        Service savedService = serviceRepository.save(service);
        return mapToDto(savedService);
    }

    @Transactional(readOnly = true)
    public List<ServiceDto> getMyServices(String prestataireEmail) {
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));

        List<Service> services = serviceRepository.findByPrestataireId(prestataire.getId());
        return services.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceDto getMyServiceById(Long serviceId, String prestataireEmail) {
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        // Security Check: Ensure the service belongs to the logged-in prestataire
        if (!service.getPrestataire().getId().equals(prestataire.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce service.");
        }

        return mapToDto(service);
    }


    @Transactional
    public ServiceDto updateService(Long serviceId, ServiceCreateUpdateDto serviceDto, String prestataireEmail) {
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        // Security Check: Ensure the service belongs to the logged-in prestataire
        if (!service.getPrestataire().getId().equals(prestataire.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce service.");
        }

        service.setNom(serviceDto.getNom());
        service.setDescription(serviceDto.getDescription());
        service.setPrix(serviceDto.getPrix());

        Service updatedService = serviceRepository.save(service);
        return mapToDto(updatedService);
    }

    @Transactional
    public void deleteService(Long serviceId, String prestataireEmail) {
        Prestataire prestataire = prestataireRepository.findByEmail(prestataireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", prestataireEmail));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        // Security Check: Ensure the service belongs to the logged-in prestataire
        if (!service.getPrestataire().getId().equals(prestataire.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer ce service.");
        }

        serviceRepository.deleteById(serviceId);
    }


    // --- Méthode utilitaire pour mapper Entité vers DTO ---
    private ServiceDto mapToDto(Service service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setNom(service.getNom());
        dto.setDescription(service.getDescription());
        dto.setPrix(service.getPrix());
        dto.setPrestataireId(service.getPrestataire().getId());
        dto.setPrestataireNom(service.getPrestataire().getNom()); // Assurez-vous que Prestataire a une méthode getNom()
        return dto;
    }

    // Ajoutez ici d'autres méthodes du service Prestataire si nécessaire
    // (ex: getCurrentPrestataireDetails, getMyDisponibilites, etc.)

    // --- NOUVELLES Méthodes pour la gestion des Disponibilités (ÉTAPE 7) ---

    @Transactional
    public DisponibiliteDto addDisponibilite(DisponibiliteCreateDto dispoDto, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail);

        // Validation Logique
        if (dispoDto.getDateDebut() == null || dispoDto.getDateFin() == null) {
            throw new BadRequestException("Les dates de début et de fin ne peuvent pas être nulles.");
        }
        if (!dispoDto.getDateFin().isAfter(dispoDto.getDateDebut())) {
            throw new BadRequestException("La date de fin doit être postérieure à la date de début.");
        }
        // Vérification des chevauchements
        List<Disponibilite> overlaps = disponibiliteRepository.findByPrestataireIdAndDateDebutLessThanAndDateFinGreaterThan(
                prestataire.getId(), dispoDto.getDateFin(), dispoDto.getDateDebut()
        );
        if (!overlaps.isEmpty()) {
            throw new BadRequestException("Cette période de disponibilité entre en conflit avec une période existante.");
        }

        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setDateDebut(dispoDto.getDateDebut());
        disponibilite.setDateFin(dispoDto.getDateFin());
        disponibilite.setPrestataire(prestataire);

        Disponibilite savedDisponibilite = disponibiliteRepository.save(disponibilite);
        return mapDisponibiliteToDto(savedDisponibilite);
    }

    @Transactional(readOnly = true)
    public List<DisponibiliteDto> getMyDisponibilites(String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail);
        List<Disponibilite> disponibilites = disponibiliteRepository.findByPrestataireIdOrderByDateDebutAsc(prestataire.getId());
        return disponibilites.stream().map(this::mapDisponibiliteToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteDisponibilite(Long disponibiliteId, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail);
        // Trouve la disponibilité ET vérifie qu'elle appartient bien au prestataire connecté
        Disponibilite disponibilite = disponibiliteRepository.findByIdAndPrestataireId(disponibiliteId, prestataire.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilite", "id", disponibiliteId + " pour ce prestataire"));

        disponibiliteRepository.delete(disponibilite);
    }
    // --- Méthode utilitaire pour mapper Entité Disponibilite vers DTO ---
    private DisponibiliteDto mapDisponibiliteToDto(Disponibilite disponibilite) {
        DisponibiliteDto dto = new DisponibiliteDto();
        dto.setId(disponibilite.getId());
        dto.setDateDebut(disponibilite.getDateDebut());
        dto.setDateFin(disponibilite.getDateFin());
        if (disponibilite.getPrestataire() != null) { // Check for null
            dto.setPrestataireId(disponibilite.getPrestataire().getId());
        }
        return dto;
    }
    @Transactional(readOnly = true)
    public PrestataireDto getCurrentPrestataireDetails(String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Réutilise la méthode privée existante
        return mapPrestataireToDto(prestataire); // Appelle la nouvelle méthode de mapping
    }
}