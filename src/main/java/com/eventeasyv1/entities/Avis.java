package com.eventeasyv1.entities;

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
@Table(name = "avis")
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // Pour les textes longs
    @Column(nullable = false)
    private String commentaire;

    @Column(nullable = false) // Note de 1 à 5 par exemple
    private int note; // Ou Integer

    @CreationTimestamp
    @Column(name = "date_avis", nullable = false, updatable = false)
    private LocalDateTime dateAvis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client; // Client qui a laissé l'avis

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service; // Service concerné par l'avis
}