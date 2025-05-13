package com.eventeasyv1.dto.input;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EvenementUpdateDto {

    @Size(min = 3, max = 150, message = "Le nom doit contenir entre 3 et 150 caractères.")
    private String nom;

    private String description;

    @FutureOrPresent(message = "La date de l'événement doit être dans le présent ou le futur.")
    private LocalDate dateEvenement;

    @Size(max = 255, message = "Le lieu ne peut pas dépasser 255 caractères.")
    private String lieu;
}