package com.eventeasyv1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // Pour les champs obligatoires
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) utilisé pour transporter les données
 * lors de l'inscription d'un Client ou d'un Prestataire.
 * Contient les champs communs et ceux spécifiques au Prestataire.
 */
@Data // Génère automatiquement getters, setters, toString, equals, hashCode
public class RegisterRequest {

    // --- Champs communs (Utilisateur) ---

    @NotBlank(message = "Le nom est obligatoire")
    // Pour un Prestataire, cela pourrait être le nom du contact principal.
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    // Pour un Prestataire, cela pourrait être le prénom du contact principal.
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être une adresse email valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    // --- Champs spécifiques ajoutés pour Prestataire ---
    // Assurez-vous que votre formulaire frontend envoie ces données
    // lors de l'inscription d'un prestataire.

    @NotBlank(message = "Le nom de l'entreprise est obligatoire") // Validation ajoutée
    private String nomEntreprise;

    @NotBlank(message = "La catégorie de service est obligatoire") // Validation ajoutée
    private String categorieService;

    // Validation non ajoutée pour adresse et numéro, car potentiellement optionnels.
    // Ajoutez @NotBlank si nécessaire.
    private String adresse;

    private String numeroTel; // Nommé numeroTel pour correspondre au champ Java
    // Mappé sur numero_tel dans la DB via @Column
}