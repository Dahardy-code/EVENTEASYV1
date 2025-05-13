package com.eventeasyv1.entities;

import com.eventeasyv1.entities.enums.StatutReservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateReservation; // Date ET heure de l'événement réservé

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutReservation statut;

    @Column(name = "prix_final", precision = 10, scale = 2) // Nom de colonne explicite
    private BigDecimal prixFinal; // Prix au moment de la réservation, copié depuis le service

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation; // Date de création de l'enregistrement de réservation

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
}