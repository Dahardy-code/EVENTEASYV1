package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateDto {

    @NotNull(message = "L'ID du service ne peut pas être nul.")
    private Long serviceId;

    @NotNull(message = "La date de réservation ne peut pas être nulle.")
    @FutureOrPresent(message = "La date de réservation doit être dans le présent ou le futur.")
    private LocalDateTime dateReservation; // Date et heure souhaitées pour le service
}