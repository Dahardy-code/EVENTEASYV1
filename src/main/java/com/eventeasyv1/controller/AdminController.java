package com.eventeasyv1.controller;

import com.eventeasyv1.dao.AdminRepository;
// L'import de Administrateur n'est pas nécessaire pour cette méthode spécifique.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // Pour sécuriser l'endpoint
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // Chemin de base pour les API admin
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // Exemple d'API pour obtenir le nombre d'administrateurs actifs
    @GetMapping("/admins/count-active") // Endpoint plus spécifique
    @PreAuthorize("hasRole('ADMIN')") // Sécuriser cet endpoint
    public long countActiveAdmins() {
        return adminRepository.countByActive(true); // Appel de la méthode qui va maintenant exister
    }

    @GetMapping("/admins/count-inactive") // Endpoint exemple pour les inactifs
    @PreAuthorize("hasRole('ADMIN')")
    public long countInactiveAdmins() {
        return adminRepository.countByActive(false);
    }
}