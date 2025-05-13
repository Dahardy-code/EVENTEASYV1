package com.eventeasyv1.service;

import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface AvisService {
    AvisDto addAvisToService(Long serviceId, AvisCreateDto avisCreateDto, Authentication authentication);
    Page<AvisDto> getAvisForService(Long serviceId, Pageable pageable);
    Page<AvisDto> getAvisForMyServices(Authentication authentication, Pageable pageable); // Pour le prestataire
    // Optionnel :
    // AvisDto updateMyAvis(Long avisId, AvisUpdateDto avisUpdateDto, Authentication authentication);
    // void deleteMyAvis(Long avisId, Authentication authentication); // Ou par Admin
}
