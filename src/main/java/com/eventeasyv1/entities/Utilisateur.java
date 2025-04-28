package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED) // Stratégie pour l'héritage
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING) // Colonne pour différencier les rôles
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String password; // Renommé en password pour la clarté avec Spring Security

    @Column(name = "role", insertable = false, updatable = false) // Mappé par DiscriminatorColumn
    private String role;
}