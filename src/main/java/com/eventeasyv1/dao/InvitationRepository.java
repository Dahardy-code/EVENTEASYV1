package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEvenementId(Long evenementId);
    List<Invitation> findByClientId(Long clientId);
}