package com.eventeasyv1.service;

import com.eventeasyv1.dto.AvisDto;
import com.eventeasyv1.dto.input.AvisCreateDto;

import java.util.List;

public interface AvisService {
    List<AvisDto> getAvisByServiceId(Long serviceId);
    AvisDto createAvis(Long serviceId, AvisCreateDto avisDto);
}