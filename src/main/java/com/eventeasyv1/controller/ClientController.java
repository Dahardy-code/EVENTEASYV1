package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ClientDto;
import com.eventeasyv1.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
// @CrossOrigin(origins = "*") // Ou configurez globalement dans SecurityConfig
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Endpoint pour obtenir les infos du client connecté
    @GetMapping("/me")
    public ResponseEntity<ClientDto> getCurrentClientInfo() {
        // La logique pour récupérer l'utilisateur connecté est gérée par ClientService
        // grâce au SecurityContextHolder rempli par JwtFilter
        try {
            ClientDto clientInfo = clientService.getCurrentClientDetails();
            return ResponseEntity.ok(clientInfo);
        } catch (Exception e) {
            // Gérer le cas où le client n'est pas trouvé (ne devrait pas arriver si le token est valide)
            return ResponseEntity.status(404).build();
        }
    }

    // Ajoutez ici d'autres endpoints spécifiques aux clients (réservations, etc.)
    // Exemple:
    // @GetMapping("/reservations")
    // public ResponseEntity<?> getClientReservations() { ... }
}