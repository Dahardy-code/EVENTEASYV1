package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "avis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // Pour les champs TEXT longs
    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "note")
    private Integer note;

    @Column(name = "date_avis")
    private LocalDateTime dateAvis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id") // Clé étrangère vers client
    @ToString.Exclude
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id") // Clé étrangère vers service
    @ToString.Exclude
    private Service service;
}