package com.eventeasyv1.utils;

import org.springframework.stereotype.Component;

@Component
public class ChatbotUtil {

    // Ce serait ici que vous intégreriez une API de chatbot externe
    // (Dialogflow, Rasa, etc.) ou votre propre logique NLP simple.

    public String getResponse(String userMessage) {
        // TODO: Implémenter la logique de réponse du chatbot
        String lowerCaseMessage = userMessage.toLowerCase();
        if (lowerCaseMessage.contains("bonjour") || lowerCaseMessage.contains("salut")) {
            return "Bonjour ! Comment puis-je vous aider avec l'organisation de votre événement ?";
        } else if (lowerCaseMessage.contains("prix") || lowerCaseMessage.contains("tarif")) {
            return "Pour connaître les tarifs, je vous invite à consulter la page détail du service ou à contacter directement le prestataire.";
        } else if (lowerCaseMessage.contains("disponible") || lowerCaseMessage.contains("dispo")) {
            return "Vous pouvez vérifier les disponibilités sur le calendrier du prestataire ou le contacter.";
        } else {
            return "Je ne suis pas sûr de comprendre. Pouvez-vous reformuler ou chercher un service spécifique ?";
        }
    }
}