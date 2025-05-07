package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByPrestataireId(Long prestataireId);
    List<Service> findByCategorie(String categorie);

    // Exemple de recherche paginée avec filtres optionnels
    // Utilise JPQL (Java Persistence Query Language)
    @Query("SELECT s FROM Service s WHERE " +
            "(:prestataireId IS NULL OR s.prestataire.id = :prestataireId) AND " +
            "(:categorie IS NULL OR LOWER(s.categorie) = LOWER(:categorie)) AND " +
            "(:query IS NULL OR LOWER(s.titre) LIKE LOWER(concat('%', :query, '%')) OR LOWER(s.description) LIKE LOWER(concat('%', :query, '%')))")
    Page<Service> findServices(
            @Param("query") String query,
            @Param("categorie") String categorie,
            @Param("prestataireId") Long prestataireId, // Peut être utile pour filtrer par prestataire
            Pageable pageable);

    <T> ScopedValue<T> findByIdAndPrestataireId(Long serviceId, Long id);

    @Query("SELECT s FROM Service s WHERE s.prestataire.id = :id")
    Page<Service> findServicesByPrestataireId(@Param("id") Long id, Pageable pageable);

    Page<Service> searchServices(String searchTerm, String categorieFiltre, Pageable pageable);
}