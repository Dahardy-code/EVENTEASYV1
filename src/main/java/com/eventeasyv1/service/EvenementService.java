package com.eventeasyv1.service;
import com.eventeasyv1.dto.EvenementDto;
import com.eventeasyv1.dto.input.EvenementCreateDto;
import com.eventeasyv1.dto.input.EvenementUpdateDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface EvenementService {
    EvenementDto createEvenement(EvenementCreateDto evenementDto, Authentication authentication);
    EvenementDto getEvenementByIdForClient(Long evenementId, Authentication authentication); // Renommé pour clarté
    List<EvenementDto> getMyEvenements(Authentication authentication);
    EvenementDto updateEvenement(Long evenementId, EvenementUpdateDto evenementDto, Authentication authentication);
    void deleteEvenement(Long evenementId, Authentication authentication);
}