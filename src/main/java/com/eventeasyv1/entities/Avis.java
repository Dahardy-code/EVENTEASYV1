package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "avis")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int note; // 1 à 5
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
