// src/main/java/com/eventeasyv1/dto/LoginRequest.java
package com.eventeasyv1.dto;

import jakarta.validation.constraints.Email; // Correct import
import jakarta.validation.constraints.NotBlank; // Correct import
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}