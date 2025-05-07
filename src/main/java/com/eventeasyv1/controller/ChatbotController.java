package com.eventeasyv1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    // TODO: Injecter ChatbotService

    @PostMapping("/message")
    public ResponseEntity<?> handleMessage(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Message cannot be empty.");
        }

        // TODO: Implémenter la logique du chatbot dans ChatbotService
        String botResponse = "Ceci est une réponse placeholder du chatbot pour : '" + userMessage + "'"; // Placeholder

        return ResponseEntity.ok(Map.of("reply", botResponse));
    }
}