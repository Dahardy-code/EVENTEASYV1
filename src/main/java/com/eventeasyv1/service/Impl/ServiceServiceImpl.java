package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.ServiceRepository;
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.entities.Service;
import com.eventeasyv1.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ServiceServiceImpl implements ServiceService {

    private static final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<ServiceDto> getAllPublicServices(Pageable pageable, String categorieFiltre, String searchTerm) {
        log.debug("Récupération des services publics. Page: {}, Catégorie: {}, Terme: {}",
                pageable.getPageNumber(), categorieFiltre, searchTerm);
        Page<Service> servicesPage;

        // Utilisation de la méthode de recherche personnalisée du repository
        servicesPage = serviceRepository.searchServices(searchTerm, categorieFiltre, pageable);

        return servicesPage.map(this::mapEntityToDto);
    }

    @Override
    public Optional<ServiceDto> getPublicServiceById(Long id) {
        log.debug("Récupération du service public ID: {}", id);
        return serviceRepository.findById(id).map(this::mapEntityToDto);
    }

    private ServiceDto mapEntityToDto(Service service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setTitre(service.getTitre());
        dto.setDescription(service.getDescription());
        dto.setPrix(service.getPrix());
        dto.setCategorie(service.getCategorie());
        if (service.getPrestataire() != null) {
            dto.setPrestataireId(service.getPrestataire().getId());
            dto.setNomEntreprisePrestataire(service.getPrestataire().getNomEntreprise());
        }
        return dto;
    }
}