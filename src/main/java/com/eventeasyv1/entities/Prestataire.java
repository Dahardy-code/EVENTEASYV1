// src/main/java/com/eventeasyv1/entities/Prestataire.java
package com.eventeasyv1.entities;

import jakarta.persistence.*; // Import the whole package or specific annotations
import lombok.Data; // Assuming you use Lombok for getters/setters

@Entity
@Data // Add if you use Lombok, otherwise keep manual getters/setters
public class Prestataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Good practice for auto-generated IDs
    private Long id;
    private String nom;
    private String email;
    private String téléphone;
    private String adresse;

    // Remove manual getters/setters if using @Data
    /*
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... other getters/setters
    */
}