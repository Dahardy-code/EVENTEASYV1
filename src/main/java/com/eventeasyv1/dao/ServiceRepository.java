package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByPrestataireId(Long prestataireId);
    Page<Service> findByPrestataireId(Long prestataireId, Pageable pageable);
    Page<Service> findByCategorieContainingIgnoreCase(String categorie, Pageable pageable);
    Page<Service> findByTitreContainingIgnoreCase(String titre, Pageable pageable);

    // Recherche plus complexe combinant titre et catégorie
    @Query("SELECT s FROM Service s WHERE " +
            "(:query IS NULL OR LOWER(s.titre) LIKE LOWER(concat('%', :query, '%')) OR LOWER(s.description) LIKE LOWER(concat('%', :query, '%'))) AND " +
            "(:categorie IS NULL OR LOWER(s.categorie) = LOWER(:categorie))")
    Page<Service> searchServices(
            @Param("query") String query,
            @Param("categorie") String categorie,
            Pageable pageable
    );
}