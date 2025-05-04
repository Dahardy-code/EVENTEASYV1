package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime; // Use LocalDateTime for date and time

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disponibilites") // Explicit table name (optional but good practice)
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDebut; // Start date and time

    @Column(nullable = false)
    private LocalDateTime dateFin;   // End date and time

    // Many availabilities can belong to one provider
    @ManyToOne(fetch = FetchType.LAZY) // LAZY is generally better for performance
    @JoinColumn(name = "prestataire_id", nullable = false) // Foreign key column in 'disponibilites' table
    private Prestataire prestataire;

    // Lombok handles constructors, getters, setters
}