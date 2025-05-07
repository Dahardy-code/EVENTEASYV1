package com.eventeasyv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Configuration de base, à adapter avec vos propriétés réelles
    // dans application.properties (spring.mail.*)
    // Spring Boot peut souvent auto-configurer JavaMailSender si les propriétés sont présentes.
    // Ce bean explicite n'est nécessaire que pour une configuration plus fine.

    /* Décommentez et configurez si l'auto-configuration ne suffit pas
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configurer via application.properties (préféré)
        // Exemples:
        // mailSender.setHost("smtp.gmail.com");
        // mailSender.setPort(587);
        // mailSender.setUsername("my.email@gmail.com");
        // mailSender.setPassword("my-password-or-app-password");

        // Properties properties = mailSender.getJavaMailProperties();
        // properties.put("mail.transport.protocol", "smtp");
        // properties.put("mail.smtp.auth", "true");
        // properties.put("mail.smtp.starttls.enable", "true");
        // properties.put("mail.debug", "true"); // Pour le développement

        return mailSender;
    }
    */
}