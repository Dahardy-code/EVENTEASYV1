package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString; // Importer pour @ToString.Exclude

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// Assurez-vous que cet import est correct et que Reservation.java existe et est une @Entity
import com.eventeasyv1.entities.Reservation;


@Entity
@Table(name = "client") // Pointe vers la table spécifique 'client'
@PrimaryKeyJoinColumn(name = "id") // Indique que 'id' est PK et FK vers utilisateur.id
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"reservations"}) // Exclure les collections
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Utilisateur {

    @Temporal(TemporalType.DATE) // Correspond au type DATE de la DB
    @Column(name = "date_inscription") // Nom de colonne correct
    private Date dateInscription;

    // --- Relations ---
    // Un Client peut avoir plusieurs Réservations
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude // Important pour éviter les boucles infinies
    private List<Reservation> reservations = new ArrayList<>();

    // Ajoutez d'autres relations ici si nécessaire (Avis, etc.) en pensant à les exclure de EqualsAndHashCode

    @Override
    public String toString() {
        // Fournir une implémentation toString plus sûre que celle de Lombok par défaut
        return "Client{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", dateInscription=" + dateInscription +
                ", reservationsCount=" + (reservations != null ? reservations.size() : 0) +
                '}';
    }
}