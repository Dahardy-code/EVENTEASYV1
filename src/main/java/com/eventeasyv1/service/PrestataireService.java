package com.eventeasyv1.service;

import com.eventeasyv1.dao.PrestataireRepository;
import com.eventeasyv1.dao.ServiceRepository;
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;
import com.eventeasyv1.entities.Prestataire;
import com.eventeasyv1.entities.Service;
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
}