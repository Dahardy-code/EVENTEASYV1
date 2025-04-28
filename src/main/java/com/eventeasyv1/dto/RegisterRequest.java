// src/main/java/com/eventeasyv1/dto/RegisterRequest.java
package com.eventeasyv1.dto;

import jakarta.validation.constraints.Email; // Correct import
import jakarta.validation.constraints.NotBlank; // Correct import
import jakarta.validation.constraints.Size; // Correct import
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
}