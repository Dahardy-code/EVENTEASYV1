package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.AvisRepository;
import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.ReservationRepository;
import com.eventeasyv1.dao.ServiceRepository;
import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;
import com.eventeasyv1.entities.Avis;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Service;
import com.eventeasyv1.service.AvisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvisServiceImpl implements AvisService {

    private static final Logger log = LoggerFactory.getLogger(AvisServiceImpl.class);

    @Autowired
    private AvisRepository avisRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AvisDto> getAvisByServiceId(Long serviceId) {
        log.debug("Recherche des avis pour le service ID: {}", serviceId);
        List<Avis> avisList = avisRepository.findByServiceId(serviceId);
        return avisList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AvisDto createAvis(Long serviceId, AvisCreateDto avisDto) {
        log.info("Tentative de création d'avis pour le service ID: {}", serviceId);
        Long clientId = 1L; // Placeholder for client ID retrieval

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé ID: " + serviceId));

        Client clientRef = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé ID: " + clientId));

        Avis avis = new Avis();
        avis.setCommentaire(avisDto.getCommentaire());
        avis.setNote(avisDto.getNote());
        avis.setDateAvis(LocalDateTime.now());
        avis.setClient(clientRef);
        avis.setService(service);

        avisRepository.save(avis);

        return mapToDto(avis);
    }

    private AvisDto mapToDto(Avis avis) {
        AvisDto dto = new AvisDto();
        dto.setId(avis.getId());
        dto.setCommentaire(avis.getCommentaire());
        dto.setNote(avis.getNote());
        dto.setDateAvis(avis.getDateAvis());
        return dto;
    }
}