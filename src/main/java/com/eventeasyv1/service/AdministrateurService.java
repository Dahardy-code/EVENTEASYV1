package com.eventeasyv1.service;

import com.eventeasyv1.dao.AdminRepository;
import com.eventeasyv1.entities.Administrateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministrateurService {

    @Autowired
    private AdminRepository adminRepository;

    public Optional<Administrateur> getAdministrateurByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
