package com.eventeasyv1.entities;

import com.eventeasyv1.entities.enums.StatutInvitation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150) // Email de l'invité
    private String email;

    @Lob
    private String message;

    @CreationTimestamp
    @Column(name = "date_envoi", nullable = false, updatable = false)
    private LocalDateTime dateEnvoi;

    //Optionnel : Statut de l'invitation (ex: ENVOYEE, ACCEPTEE, REFUSEE)
    @Enumerated(EnumType.STRING)
     @Column(name = "statut_invitation", length = 50)
    private StatutInvitation statut;

    // L'invitation est envoyée par un client (l'organisateur)
    // Bien que l'événement appartienne au client, on peut garder une trace ici si un client différent envoie pour un autre.
    // Mais plus logiquement, c'est l'événement qui est lié au client.
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "client_envoyeur_id")
    // private Client clientEnvoyeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement; // Événement auquel l'invitation se rapporte
}