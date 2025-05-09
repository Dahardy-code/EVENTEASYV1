package com.eventeasyv1.service;

import com.eventeasyv1.dao.AdminRepository;
import com.eventeasyv1.entities.Administrateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurService {

    @Autowired
    private AdminRepository adminRepository;

    public Administrateur getAdministrateurByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
