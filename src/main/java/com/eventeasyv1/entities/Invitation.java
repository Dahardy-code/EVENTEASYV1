package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 150)
    private String email; // Email de l'invité

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    // Client qui envoie l'invitation (peut être redondant si lié à l'événement)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    // Événement pour lequel l'invitation est envoyée
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false) // Une invitation concerne un événement
    @ToString.Exclude
    private Evenement evenement;
}