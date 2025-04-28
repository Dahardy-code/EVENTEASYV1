package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "client")
@DiscriminatorValue("CLIENT") // Valeur pour ce type d'utilisateur
@Data
@EqualsAndHashCode(callSuper = true) // Important pour Lombok avec héritage
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Utilisateur {

    @Temporal(TemporalType.DATE)
    @Column(name = "date_inscription")
    private Date dateInscription;

    // Ajoutez ici les relations spécifiques au client si nécessaire (ex: reservations)
    // @OneToMany(mappedBy = "client")
    // private List<Reservation> reservations;
}