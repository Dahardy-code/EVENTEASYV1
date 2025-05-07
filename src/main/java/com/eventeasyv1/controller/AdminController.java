package com.eventeasyv1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Pour la sécurité au niveau méthode
import org.springframework.web.bind.annotation.*;

// TODO: Importer les services Admin nécessaires

@RestController
@RequestMapping("/api/admin") // Préfixe pour les routes admin
// @PreAuthorize("hasRole('ADMIN')") // Alternative: Sécuriser toute la classe
public class AdminController {

    // TODO: Injecter les services nécessaires (AdminService, UtilisateurService, etc.)
    // @Autowired
    // private AdminService adminService;

    // Exemple d'endpoint admin
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')") // Sécuriser la méthode
    public ResponseEntity<?> getAllUsers() {
        // TODO: Implémenter la logique pour récupérer tous les utilisateurs (avec pagination)
        return ResponseEntity.ok("Liste des utilisateurs (TODO)");
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // TODO: Implémenter la logique pour supprimer un utilisateur
        return ResponseEntity.ok("Utilisateur " + id + " supprimé (TODO)");
    }

    // Ajoutez d'autres endpoints pour la gestion des offres, des réservations, etc.
}