package com.eventeasyv1.entities;

// Corrected imports for Spring Boot 3+ / Jakarta EE
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
// Add other jakarta.persistence imports if needed (e.g., @GeneratedValue, @Column)

@Entity
public class Service {

    @Id
    // Consider adding @GeneratedValue if you want the ID generated automatically
    // e.g., @GeneratedValue(strategy = GenerationType.IDENTITY)
    // If so, you'll need: import jakarta.persistence.GeneratedValue;
    //                     import jakarta.persistence.GenerationType;
    private Long id;
    private String nom;
    private String description;
    private double prix;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}