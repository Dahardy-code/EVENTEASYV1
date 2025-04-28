package com.eventeasyv1.config; // Ou dans un package service.impl

import com.eventeasyv1.dao.UtilisateurRepository;
import com.eventeasyv1.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; // Spring Security User
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList; // Pour les GrantedAuthority vides

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        // Pour l'instant, pas de gestion fine des rôles/autorisations ici,
        // mais vous pourriez les charger depuis la base si nécessaire.
        // Le rôle est géré via le type d'entité (Client, Prestataire...).
        // On utilise l'email comme 'username' pour Spring Security.
        return new User(utilisateur.getEmail(), utilisateur.getPassword(), new ArrayList<>());
    }
}