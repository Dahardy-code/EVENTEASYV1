package com.eventeasyv1.dto.input;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvisCreateDto {

    @NotBlank(message = "Le commentaire ne peut pas être vide.")
    @Size(min = 10, max = 2000, message = "Le commentaire doit contenir entre 10 et 2000 caractères.")
    private String commentaire;

    @NotNull(message = "La note ne peut pas être nulle.")
    @Min(value = 1, message = "La note doit être au minimum de 1.")
    @Max(value = 5, message = "La note doit être au maximum de 5.")
    private Integer note; // Utiliser Integer pour permettre la validation @NotNull
}