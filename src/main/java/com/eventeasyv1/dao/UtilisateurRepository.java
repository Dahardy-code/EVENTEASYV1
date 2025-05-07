package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Trouve un utilisateur par son email.
     * @param email L'email à rechercher.
     * @return Un Optional contenant l'Utilisateur trouvé, ou vide sinon.
     */
    Optional<Utilisateur> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec l'email donné.
     * Plus efficace que findByEmail().isPresent() si on n'a besoin que de savoir l'existence.
     * @param email L'email à vérifier.
     * @return true si l'email existe, false sinon.
     */
    boolean existsByEmail(String email);
}