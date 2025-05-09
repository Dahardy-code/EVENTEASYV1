package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Importez List si vous décommentez les relations
// import java.util.List;

@Entity
@Table(name = "prestataire") // *** Pointe vers la table spécifique 'prestataire' ***
@PrimaryKeyJoinColumn(name = "id") // *** Indique que 'id' est PK et FK vers utilisateur.id ***
// On ne met PAS @DiscriminatorValue avec JOINED
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Prestataire extends Utilisateur {

    // L'ID est hérité et géré par @PrimaryKeyJoinColumn

    // Mappages explicites des colonnes spécifiques
    @Column(name = "nom_entreprise", nullable = true)
    private String nomEntreprise;

    @Column(name = "categorie_service", nullable = true)
    private String categorieService;

    @Column(name = "adresse", nullable = true)
    private String adresse;

    @Column(name = "numero_tel", nullable = true)
    private String numeroTel;

    // --- Relations ---
    // Ces relations utilisent 'prestataire_id' qui est défini dans les tables
    // service et disponibilite, donc le mappedBy devrait fonctionner.
    // @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Service> services;

    // @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Disponibilite> disponibilites;
}