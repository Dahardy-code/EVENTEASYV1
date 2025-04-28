package com.eventeasyv1.service;

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Méthode pour récupérer les détails du client actuellement connecté
    public ClientDto getCurrentClientDetails() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) clientRepository.findByEmail(userEmail) // Suppose une méthode findByEmail dans ClientRepository
                .orElseThrow(() -> new UsernameNotFoundException("Client non trouvé avec l'email: " + userEmail));

        return mapToDto(client); // Convertir l'entité en DTO
    }


    // Méthode pour mapper l'entité Client vers ClientDto
    private ClientDto mapToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        dto.setDateInscription(client.getDateInscription());
        // Ajoutez d'autres champs si nécessaire
        return dto;
    }

    // Ajoutez cette méthode à ClientRepository
    // Optional<Client> findByEmail(String email);
}