package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
// Importez List si vous décommentez la relation
// import java.util.List;

@Entity
@Table(name = "client") // *** Pointe vers la table spécifique 'client' ***
@PrimaryKeyJoinColumn(name = "id") // *** Indique que 'id' est PK et FK vers utilisateur.id ***
// On ne met PAS @DiscriminatorValue avec JOINED
@Data
@EqualsAndHashCode(callSuper = true) // Important pour Lombok et héritage
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Utilisateur {

    // L'ID est hérité et géré par @PrimaryKeyJoinColumn

    @Temporal(TemporalType.DATE) // Correspond au type DATE de la DB
    @Column(name = "date_inscription", nullable = true) // Assumer nullable si non NOT NULL
    private Date dateInscription;

    // --- Relations ---
    // La relation semble pointer vers client(id) qui est correct
    // @OneToMany(mappedBy = "client")
    // private List<Reservation> reservations;

    // @OneToMany(mappedBy = "client")
    // private List<Avis> avis;

    // @OneToMany(mappedBy = "client")
    // private List<Invitation> invitations;

    // @OneToMany(mappedBy = "client")
    // private List<Evenement> evenements;
}