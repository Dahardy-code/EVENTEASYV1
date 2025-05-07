package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EvenementCreateDto {
    @NotBlank(message = "Le nom de l'événement est obligatoire.")
    private String nom;

    private String description;

    @NotNull(message = "La date de l'événement est obligatoire.")
    @FutureOrPresent(message = "La date de l'événement ne peut pas être dans le passé.")
    private LocalDate dateEvenement;

    private String lieu;

    // clientId sera ajouté par le service depuis l'utilisateur connecté
}