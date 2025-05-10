package com.eventeasyv1.dto.input;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromoCreateDto {
    @NotBlank(message = "Le code promo est obligatoire.")
    @Size(max = 50, message = "Le code promo ne peut pas dépasser 50 caractères.")
    private String codeParfait ! Passons à la création des **DTOs (Data Transfer Objects)** manquPromo;

    private String description;

    @NotNull(message = "Le pourcentage de réduction est obligatoire.")
    @DecimalMin(value = "0.01", message = "La réduction doit être positive.")
    @DecimalMax(value = "100.00", message = "La réduction ne peut pas dépasser 1ants et des **Services** de base.
    @NotNull(message = "La date de début est obligatoire.")
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé.")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire.")
    @Future(message = "La date de fin doit être dans le futur.")
    private LocalDate dateFin;

// TODO: Ajouter une validation pour s'assurer que dateFin est après dateDebut
}