-- Script pour supprimer et recréer les tables de la base EVENTEASYV1
-- ATTENTION : CECI SUPPRIME TOUTES LES DONNÉES EXISTANTES !

-- Désactiver temporairement les vérifications de clés étrangères pour permettre la suppression
SET FOREIGN_KEY_CHECKS=0;

-- Supprimer les tables si elles existent (dans l'ordre inverse des dépendances)
DROP TABLE IF EXISTS paiement;
DROP TABLE IF EXISTS avis;
DROP TABLE IF EXISTS invitation;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS evenement;
DROP TABLE IF EXISTS disponibilite;
DROP TABLE IF EXISTS service;
DROP TABLE IF EXISTS administrateur;
DROP TABLE IF EXISTS prestataire;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS promo;
DROP TABLE IF EXISTS statistique;
DROP TABLE IF EXISTS utilisateur; -- Supprimer la table utilisateur en dernier

-- Réactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS=1;

-- Recréer les tables avec le schéma corrigé (Option 1)

-- ====================
-- TABLE UTILISATEUR (CORRIGÉE - SANS COLONNE 'role')
-- ====================
CREATE TABLE utilisateur (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             nom VARCHAR(100),
                             prenom VARCHAR(100),
                             email VARCHAR(100) UNIQUE,
                             mot_de_passe VARCHAR(255)
);

-- ====================
-- TABLE CLIENT
-- ====================
CREATE TABLE client (
                        id BIGINT PRIMARY KEY, -- Référence utilisateur.id
                        date_inscription DATE,
                        FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- ====================
-- TABLE PRESTATAIRE
-- ====================
CREATE TABLE prestataire (
                             id BIGINT PRIMARY KEY, -- Référence utilisateur.id
                             nom_entreprise VARCHAR(150),
                             categorie_service VARCHAR(100),
                             adresse VARCHAR(255),
                             numero_tel VARCHAR(50),
                             FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- ====================
-- TABLE ADMINISTRATEUR
-- ====================
CREATE TABLE administrateur (
                                id BIGINT PRIMARY KEY, -- Référence utilisateur.id
                                privileges VARCHAR(255),
                                FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- ====================
-- TABLE SERVICE
-- ====================
CREATE TABLE service (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         titre VARCHAR(150),
                         description TEXT,
                         prix DECIMAL(10,2),
                         categorie VARCHAR(100),
                         prestataire_id BIGINT,
                         FOREIGN KEY (prestataire_id) REFERENCES prestataire(id) ON DELETE CASCADE
);

-- ====================
-- TABLE EVENEMENT (Créer avant Invitation et Reservation si lié)
-- ====================
CREATE TABLE evenement (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nom VARCHAR(150),
                           description TEXT,
                           date_evenement DATE,
                           lieu VARCHAR(255),
                           client_id BIGINT,
                           FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE -- Ou SET NULL
);

-- ====================
-- TABLE RESERVATION
-- ====================
CREATE TABLE reservation (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             date_reservation DATETIME,
                             statut VARCHAR(50),
                             client_id BIGINT,
                             service_id BIGINT,
                             FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE SET NULL,
                             FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE SET NULL
);

-- ====================
-- TABLE PAIEMENT
-- ====================
CREATE TABLE paiement (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          montant DECIMAL(10,2),
                          date_paiement DATETIME,
                          mode_paiement VARCHAR(50),
                          reservation_id BIGINT,
                          FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE SET NULL
);

-- ====================
-- TABLE AVIS
-- ====================
CREATE TABLE avis (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      commentaire TEXT,
                      note INT,
                      date_avis DATETIME,
                      client_id BIGINT,
                      service_id BIGINT,
                      FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE SET NULL,
                      FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE SET NULL
);

-- ====================
-- TABLE NOTIFICATION
-- ====================
CREATE TABLE notification (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              message TEXT,
                              date_envoi DATETIME,
                              utilisateur_id BIGINT,
                              FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- ====================
-- TABLE PROMO
-- ====================
CREATE TABLE promo (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       code_promo VARCHAR(50),
                       description TEXT,
                       pourcentage_reduction DECIMAL(5,2),
                       date_debut DATE,
                       date_fin DATE
);

-- ====================
-- TABLE DISPONIBILITE
-- ====================
CREATE TABLE disponibilite (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               date_dispo DATE,
                               heure_debut TIME,
                               heure_fin TIME,
                               prestataire_id BIGINT,
                               FOREIGN KEY (prestataire_id) REFERENCES prestataire(id) ON DELETE CASCADE
);

-- ====================
-- TABLE INVITATION
-- ====================
CREATE TABLE invitation (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            email VARCHAR(150),
                            message TEXT,
                            date_envoi DATETIME,
                            client_id BIGINT,
                            evenement_id BIGINT,
                            FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE SET NULL,
                            FOREIGN KEY (evenement_id) REFERENCES evenement(id) ON DELETE CASCADE
);


-- ====================
-- TABLE STATISTIQUE
-- ====================
CREATE TABLE statistique (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             type_statistique VARCHAR(100),
                             valeur DECIMAL(10,2),
                             date_statistique DATE
);

-- Afficher un message de succès (optionnel, dépend de l'outil SQL)
-- SELECT 'Tables dropped and recreated successfully.' as '';