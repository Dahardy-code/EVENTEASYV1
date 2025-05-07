package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvitationCreateDto {
    @NotBlank(message = "L'email de l'invité est obligatoire.")
    @Email(message = "L'email de l'invité doit être valide.")
    private String email;

    private String message; // Peut être optionnel ou généré

    // eventId sera dans l'URL (@PathVariable)
    // clientId sera récupéré depuis l'utilisateur connecté
}