package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utilisateur") // Table de base pour les champs communs
@Inheritance(strategy = InheritanceType.JOINED) // Stratégie pour tables séparées liées par ID
@Data
@NoArgsConstructor
@AllArgsConstructor
// 'abstract' car un utilisateur doit être un Client, Prestataire, ou Admin
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // L'ID est généré dans cette table
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email", unique = true) // unique = true correspond au schéma
    private String email;

    @Column(name = "mot_de_passe") // Mapper explicitement le nom de colonne DB
    private String password; // Nom du champ Java peut rester 'password'
    // --- CHAMP AJOUTÉ ---
    @Column(name = "est_actif", nullable = false)
    private boolean active = true; // Valeur par défaut à true lors de la création
    // La colonne 'role' n'est PAS mappée ici avec JOINED.
    // Le type (et donc le rôle implicite) est déterminé par la table jointe.
}