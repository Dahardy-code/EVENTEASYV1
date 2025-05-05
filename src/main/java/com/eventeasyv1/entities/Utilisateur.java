package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utilisateur") // Table de base pour les champs communs
@Inheritance(strategy = InheritanceType.JOINED) // *** Stratégie corrigée pour correspondre à la DB ***
// On ne met PAS @DiscriminatorColumn avec la stratégie JOINED
@Data
@NoArgsConstructor
@AllArgsConstructor
// 'abstract' est pertinent ici car un utilisateur DOIT être Client ou Prestataire
public abstract class Utilisateur {

    @Id
    // L'ID est généré uniquement dans cette table de base
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assumer nullable=true si non spécifié NOT NULL dans le CREATE TABLE
    @Column(name = "nom", nullable = true)
    private String nom;

    @Column(name = "prenom", nullable = true)
    private String prenom;

    // unique=true correspond au schéma
    @Column(name = "email", unique = true, nullable = true)
    private String email;

    // Mapper explicitement le nom de la colonne DB
    @Column(name = "mot_de_passe", nullable = true)
    private String password; // Nom du champ en Java reste 'password'

    // La colonne 'role' de la DB n'est pas mappée ici avec JOINED.
    // Le type est déterminé par la table jointe (client, prestataire).

}