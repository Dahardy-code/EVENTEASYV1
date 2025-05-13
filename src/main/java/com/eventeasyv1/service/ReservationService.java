package com.eventeasyv1.service;

import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.dto.input.ReservationCreateDto;
import org.springframework.security.core.Authentication;

public interface ReservationService {
    ReservationDto createReservation(ReservationCreateDto reservationCreateDto, Authentication authentication);
    ReservationDto getReservationByIdForUser(Long reservationId, Authentication authentication);
    // Autres méthodes possibles: cancelReservation, confirmReservationByPrestataire, etc.
}