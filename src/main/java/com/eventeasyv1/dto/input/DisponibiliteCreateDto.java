 package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DisponibiliteCreateDto {

    @NotNull(message = "La date de début ne peut pas être nulle.")
    // Consider adding @FutureOrPresent if needed based on rules
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin ne peut pas être nulle.")
    // Validation logic (fin > debut) will be in the service
    private LocalDateTime dateFin;
}
