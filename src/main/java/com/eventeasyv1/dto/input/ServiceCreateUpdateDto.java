package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceCreateUpdateDto {
    @NotBlank(message = "Le titre est obligatoire.")
    private String titre;

    private String description; // Peut être optionnel

    @NotNull(message = "Le prix est obligatoire.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif.") // Ou inclusive=true si gratuit est permis
    private BigDecimal prix;

    @NotBlank(message = "La catégorie est obligatoire.")
    private String categorie;

    // prestataireId sera ajouté par le service depuis l'utilisateur connecté
}