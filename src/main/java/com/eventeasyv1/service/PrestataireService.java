package com.eventeasyv1.service;

import com.eventeasyv1.dao.DisponibiliteRepository;
import com.eventeasyv1.dao.PrestataireRepository;
import com.eventeasyv1.dao.ServiceRepository;
import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.dto.PrestataireDto; // Assurez-vous que cet import est présent
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.input.DisponibiliteCreateDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;
import com.eventeasyv1.entities.Disponibilite;
import com.eventeasyv1.entities.Prestataire;
import com.eventeasyv1.entities.Service;
import com.eventeasyv1.exception.BadRequestException;
import com.eventeasyv1.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service; // <--- CHANGÉ de @Component à @Service (plus sémantique)
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service // Utilisation de @Service pour la couche métier
public class PrestataireService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;


    // --- Méthode pour l'API /me (Étape 5 - Partie Prestataire) ---
    // Placée ici pour une meilleure organisation

    @Transactional(readOnly = true)
    public PrestataireDto getCurrentPrestataireDetails(String prestataireEmail) {
        // Réutilise la méthode privée existante (ou celle ajoutée ci-dessous) pour trouver le prestataire
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail);
        // Appelle la méthode de mapping ajoutée ci-dessous
        return mapPrestataireToDto(prestataire);
    }


    // --- Méthodes pour la gestion des Services (Étape 6) ---

    @Transactional
    public ServiceDto addService(ServiceCreateUpdateDto serviceDto, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper

        Service service = new Service();
        service.setNom(serviceDto.getNom());
        service.setDescription(serviceDto.getDescription());
        service.setPrix(serviceDto.getPrix());
        service.setPrestataire(prestataire);

        Service savedService = serviceRepository.save(service);
        return mapServiceToDto(savedService); // Renommé mapToDto en mapServiceToDto pour clarté
    }

    @Transactional(readOnly = true)
    public List<ServiceDto> getMyServices(String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        List<Service> services = serviceRepository.findByPrestataireId(prestataire.getId()); // Utilisez findByPrestataireId si vous n'avez pas ajouté le tri
        return services.stream().map(this::mapServiceToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceDto getMyServiceById(Long serviceId, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        Service service = findServiceByIdAndCheckOwnership(serviceId, prestataire); // Appel helper
        return mapServiceToDto(service);
    }

    @Transactional
    public ServiceDto updateService(Long serviceId, ServiceCreateUpdateDto serviceDto, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        Service service = findServiceByIdAndCheckOwnership(serviceId, prestataire); // Appel helper

        service.setNom(serviceDto.getNom());
        service.setDescription(serviceDto.getDescription());
        service.setPrix(serviceDto.getPrix());

        Service updatedService = serviceRepository.save(service);
        return mapServiceToDto(updatedService);
    }

    @Transactional
    public void deleteService(Long serviceId, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        Service service = findServiceByIdAndCheckOwnership(serviceId, prestataire); // Appel helper
        serviceRepository.delete(service); // Préférable de passer l'entité pour le contexte de persistance
    }


    // --- Méthodes pour la gestion des Disponibilités (Étape 7) ---

    @Transactional
    public DisponibiliteDto addDisponibilite(DisponibiliteCreateDto dispoDto, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper

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
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        List<Disponibilite> disponibilites = disponibiliteRepository.findByPrestataireIdOrderByDateDebutAsc(prestataire.getId());
        return disponibilites.stream().map(this::mapDisponibiliteToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteDisponibilite(Long disponibiliteId, String prestataireEmail) {
        Prestataire prestataire = findPrestataireByEmail(prestataireEmail); // Appel helper
        Disponibilite disponibilite = disponibiliteRepository.findByIdAndPrestataireId(disponibiliteId, prestataire.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilite", "id", disponibiliteId + " pour ce prestataire"));

        disponibiliteRepository.delete(disponibilite);
    }


    // --- Méthodes utilitaires privées (Helpers) ---

    // Helper pour trouver Prestataire par email (évite la duplication)
    private Prestataire findPrestataireByEmail(String email) {
        return prestataireRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", "email", email));
    }

    // Helper pour trouver Service par ID et vérifier la propriété
    private Service findServiceByIdAndCheckOwnership(Long serviceId, Prestataire prestataire) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
        if (!service.getPrestataire().getId().equals(prestataire.getId())) {
            throw new AccessDeniedException("Accès non autorisé à ce service.");
        }
        return service;
    }

    // Helper pour mapper Service vers ServiceDto
    private ServiceDto mapServiceToDto(Service service) { // <--- Renommé pour la clarté
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setNom(service.getNom());
        dto.setDescription(service.getDescription());
        dto.setPrix(service.getPrix());
        if(service.getPrestataire() != null) { // Vérification nullité avant accès
            dto.setPrestataireId(service.getPrestataire().getId());
            dto.setPrestataireNom(service.getPrestataire().getNom());
        }
        return dto;
    }

    // Helper pour mapper Disponibilite vers DisponibiliteDto
    private DisponibiliteDto mapDisponibiliteToDto(Disponibilite disponibilite) {
        DisponibiliteDto dto = new DisponibiliteDto();
        dto.setId(disponibilite.getId());
        dto.setDateDebut(disponibilite.getDateDebut());
        dto.setDateFin(disponibilite.getDateFin());
        if (disponibilite.getPrestataire() != null) {
            dto.setPrestataireId(disponibilite.getPrestataire().getId());
        }
        return dto;
    }

    // AJOUTÉ : Helper pour mapper Prestataire vers PrestataireDto (manquant)
    private PrestataireDto mapPrestataireToDto(Prestataire prestataire) {
        PrestataireDto dto = new PrestataireDto();
        // Copier les champs hérités
        dto.setId(prestataire.getId());
        dto.setNom(prestataire.getNom());
        dto.setPrenom(prestataire.getPrenom());
        dto.setEmail(prestataire.getEmail());
        dto.setRole("PRESTATAIRE"); // Ou lire depuis l'entité si nécessaire

        // Copier les champs spécifiques
        dto.setNomEntreprise(prestataire.getNomEntreprise());
        dto.setCategorieService(prestataire.getCategorieService());
        dto.setAdresse(prestataire.getAdresse());
        dto.setNumeroTel(prestataire.getNumeroTel());
        dto.setDescription(prestataire.getDescription());
        dto.setSiteWeb(prestataire.getSiteWeb());
        dto.setDateInscription(prestataire.getDateInscription());
        dto.setEstVerifie(prestataire.isEstVerifie());

        return dto;
    }
}