package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
public class DisponibiliteCreateDto {
    @NotNull(message = "La date est obligatoire.")
    @FutureOrPresent(message = "La date ne peut pas être dans le passé.")
    private LocalDate dateDispo;

    @NotNull(message = "L'heure de début est obligatoire.")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire.")
    private LocalTime heureFin;

    public LocalDate getDateDebut() {
        return dateDispo;
    }

    public OffsetDateTime getDebut() {
        return OffsetDateTime.of(dateDispo, heureDebut, null);
    }

    public LocalDate getDateFin() {
        return dateDispo;
    }

    // TODO: Ajouter une validation pour s'assurer que heureFin est après heureDebut
}