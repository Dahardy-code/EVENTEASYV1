package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationCreateDto {
    @NotNull(message = "L'ID du service est obligatoire.")
    private Long serviceId;

    @NotNull(message = "La date de réservation est obligatoire.")
    @Future(message = "La date de réservation doit être dans le futur.") // Ou FutureOrPresent
    private LocalDateTime dateReservation; // Ou juste LocalDate/LocalTime selon besoin

    // Le statut sera défini par le service (ex: EN_ATTENTE)
    // Le clientId sera récupéré depuis l'utilisateur connecté
}