package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.NotBlank; // For validation
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; // Ensure price is not negative
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCreateUpdateDto {

    @NotBlank(message = "Le nom du service ne peut pas être vide.")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères.")
    private String nom;

    @NotBlank(message = "La description ne peut pas être vide.")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères.")
    private String description;

    @NotNull(message = "Le prix ne peut pas être nul.")
    @PositiveOrZero(message = "Le prix doit être positif ou zéro.")
    private Double prix; // Use Double object type to allow @NotNull check

    // Note: We don't include prestataireId here.
    // It will be determined by the logged-in user in the service layer.
}