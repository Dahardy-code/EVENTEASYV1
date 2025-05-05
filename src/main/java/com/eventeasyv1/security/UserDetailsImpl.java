package com.eventeasyv1.security; // Package dédié à la sécurité

import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Prestataire;
import com.eventeasyv1.entities.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Implémentation personnalisée de UserDetails pour Spring Security.
 * Représente l'utilisateur authentifié avec ses informations essentielles
 * (ID, email, mot de passe haché, rôles/autorités).
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L; // Recommandé pour la sérialisation
    private static final Logger log = LoggerFactory.getLogger(UserDetailsImpl.class);

    private final Long id;
    private final String username; // L'email de l'utilisateur sert de username
    private final String password; // Le mot de passe HACHÉ venant de la DB
    private final Collection<? extends GrantedAuthority> authorities; // Les rôles (ex: ROLE_CLIENT)

    // Constructeur privé pour forcer l'utilisation de la méthode factory 'build'
    private UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Construit une instance de UserDetailsImpl à partir d'une entité Utilisateur.
     * Détermine le rôle en fonction du type réel de l'entité (Client, Prestataire, etc.).
     *
     * @param utilisateur L'entité Utilisateur chargée depuis la base de données.
     * @return Une instance UserDetailsImpl prête pour Spring Security.
     * @throws IllegalStateException Si le type de l'utilisateur n'est pas reconnu.
     */
    public static UserDetailsImpl build(Utilisateur utilisateur) {
        String roleString; // Le nom du rôle sans le préfixe ROLE_

        // Déterminer le rôle en utilisant 'instanceof'
        if (utilisateur instanceof Client) {
            roleString = "CLIENT";
        } else if (utilisateur instanceof Prestataire) {
            roleString = "PRESTATAIRE";
        }
        // Ajoutez ici d'autres types si votre application en a (ex: Admin)
        // else if (utilisateur instanceof Admin) {
        //     roleString = "ADMIN";
        // }
        else {
            // Si le type n'est pas reconnu, log l'erreur et lance une exception.
            // Spring Security ne pourra pas gérer un utilisateur sans rôle défini.
            log.error("Type d'utilisateur non reconnu lors de la création de UserDetails pour l'utilisateur ID {}: {}",
                    utilisateur.getId(), utilisateur.getClass().getName());
            throw new IllegalStateException("Type d'utilisateur inconnu : " + utilisateur.getClass().getName());
        }

        // Créer la collection d'autorités (rôles) attendue par Spring Security.
        // Le rôle est généralement préfixé par "ROLE_".
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleString));

        log.debug("Construction de UserDetailsImpl pour ID {}, Email {}, Rôle {}",
                utilisateur.getId(), utilisateur.getEmail(), roleString);

        // Retourner la nouvelle instance UserDetailsImpl
        return new UserDetailsImpl(
                utilisateur.getId(),
                utilisateur.getEmail(), // Utiliser l'email comme 'username'
                utilisateur.getPassword(), // Passer le mot de passe haché
                authorities); // Passer les autorités déterminées
    }

    // --- Implémentation des méthodes requises par UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Retourne les rôles (ex: [ROLE_CLIENT])
    }

    @Override
    public String getPassword() {
        return password; // Retourne le mot de passe haché
    }

    @Override
    public String getUsername() {
        return username; // Retourne l'email
    }

    // --- Méthodes d'état du compte ---
    // Mettre à true par défaut, sauf si vous gérez l'activation, le verrouillage, etc.

    @Override
    public boolean isAccountNonExpired() {
        return true; // Le compte n'expire jamais
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Le compte n'est jamais verrouillé
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants (mot de passe) n'expirent jamais
    }

    @Override
    public boolean isEnabled() {
        return true; // Le compte est toujours activé
    }

    // --- Getter supplémentaire utile ---

    public Long getId() {
        return id; // Permet d'accéder à l'ID de l'utilisateur authentifié
    }

    // --- equals() et hashCode() ---
    // Important pour la gestion des utilisateurs dans les collections ou sessions.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(id, that.id); // Comparaison basée sur l'ID unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // HashCode basé sur l'ID
    }
}