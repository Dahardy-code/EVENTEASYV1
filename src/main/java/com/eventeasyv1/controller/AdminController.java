package com.eventeasyv1.controller;

import com.eventeasyv1.dao.AdminRepository;
import com.eventeasyv1.entities.Administrateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // Exemple d'API pour obtenir le nombre d'administrateurs actifs
    @GetMapping("/countActiveAdmins")
    public long countActiveAdmins() {
        return adminRepository.countByActive(true);
    }
}
