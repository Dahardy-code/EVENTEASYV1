package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titreEvenement;
    private LocalDateTime dateReservation;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String statut; // Ex: "en attente", "confirmée", "annulée"
}
