package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.UtilisateurRepository;
import com.eventeasyv1.entities.Utilisateur;
import com.eventeasyv1.security.UserDetailsImpl; // Importer notre implémentation
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull; // Pour indiquer que l'email ne peut être null
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implémentation du service UserDetailsService de Spring Security.
 * Responsable du chargement des détails d'un utilisateur (via UserDetailsImpl)
 * à partir de la base de données en utilisant l'email comme identifiant.
 */
@Service("userDetailsService") // Nom explicite du bean pour la configuration de sécurité
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    // Injection par constructeur (préférable)
    @Autowired
    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Charge un utilisateur par son email (qui sert de "username").
     * C'est la méthode clé appelée par Spring Security lors d'une tentative d'authentification.
     *
     * @param email L'email de l'utilisateur à charger.
     * @return Une instance de UserDetails (notre UserDetailsImpl) contenant les informations de l'utilisateur.
     * @throws UsernameNotFoundException Si aucun utilisateur n'est trouvé avec cet email,
     *                                 ou si le type d'utilisateur trouvé n'est pas géré.
     */
    @Override
    @Transactional(readOnly = true) // Opération de lecture seule
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        log.debug("Tentative de chargement de l'utilisateur par email: {}", email);

        // 1. Rechercher l'utilisateur dans la base de données par son email
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> {
                    // Si non trouvé, log et lance l'exception attendue par Spring Security
                    log.warn("Utilisateur non trouvé avec l'email: {}", email);
                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
                });

        log.info("Utilisateur trouvé avec l'email: {}", email);

        // 2. Construire l'objet UserDetails en utilisant notre classe UserDetailsImpl
        try {
            // Appelle la méthode factory 'build' qui détermine le rôle etc.
            return UserDetailsImpl.build(utilisateur);
        } catch (IllegalStateException e) {
            // Gérer le cas où UserDetailsImpl.build a lancé une exception (type inconnu)
            log.error("Impossible de construire UserDetails pour l'email {}: {}", email, e.getMessage());
            // Renvoyer UsernameNotFoundException pour indiquer un problème lors du chargement
            throw new UsernameNotFoundException("Impossible de déterminer le rôle pour l'utilisateur: " + email, e);
        }
    }
}