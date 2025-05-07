package com.eventeasyv1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    @Autowired(required = false) // Mettre à false si le MailSender n'est pas toujours configuré
    private JavaMailSender emailSender;

    public boolean sendSimpleMessage(String to, String subject, String text) {
        if (emailSender == null) {
            log.warn("JavaMailSender non configuré. Impossible d'envoyer l'email à {}", to);
            // Simuler un succès en développement si pas configuré ?
            // return true;
            return false;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // TODO: Configurer l'expéditeur ('from') via application.properties (spring.mail.username)
            // message.setFrom("noreply@eventeasy.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("Email envoyé avec succès à {}", to);
            return true;
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            return false;
        }
    }

    // Ajoutez d'autres méthodes pour envoyer des emails HTML, avec pièces jointes, etc.
    // en utilisant MimeMessage et MimeMessageHelper
}