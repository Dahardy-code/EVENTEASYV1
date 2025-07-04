-- ====================
-- TABLE UTILISATEUR
-- ====================
CREATE TABLE utilisateur (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             nom VARCHAR(100),
                             prenom VARCHAR(100),
                             email VARCHAR(100) UNIQUE,
                             mot_de_passe VARCHAR(255),
                             role VARCHAR(50)
);

-- ====================
-- TABLE CLIENT
-- ====================
CREATE TABLE client (
                        id BIGINT PRIMARY KEY,
                        date_inscription DATE,
                        FOREIGN KEY (id) REFERENCES utilisateur(id)
);

-- ====================
-- TABLE PRESTATAIRE
-- ====================
CREATE TABLE prestataire (
                             id BIGINT PRIMARY KEY,
                             nom_entreprise VARCHAR(150),
                             categorie_service VARCHAR(100),
                             adresse VARCHAR(255),
                             numero_tel VARCHAR(50),
                             FOREIGN KEY (id) REFERENCES utilisateur(id)
);

-- ====================
-- TABLE ADMINISTRATEUR
-- ====================
CREATE TABLE administrateur (
                                id BIGINT PRIMARY KEY,
                                privileges VARCHAR(255),
                                FOREIGN KEY (id) REFERENCES utilisateur(id)
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
                         FOREIGN KEY (prestataire_id) REFERENCES prestataire(id)
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
                             FOREIGN KEY (client_id) REFERENCES client(id),
                             FOREIGN KEY (service_id) REFERENCES service(id)
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
                          FOREIGN KEY (reservation_id) REFERENCES reservation(id)
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
                      FOREIGN KEY (client_id) REFERENCES client(id),
                      FOREIGN KEY (service_id) REFERENCES service(id)
);

-- ====================
-- TABLE NOTIFICATION
-- ====================
CREATE TABLE notification (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              message TEXT,
                              date_envoi DATETIME,
                              utilisateur_id BIGINT,
                              FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
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
                               FOREIGN KEY (prestataire_id) REFERENCES prestataire(id)
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
                            FOREIGN KEY (client_id) REFERENCES client(id)
);

-- ====================
-- TABLE EVENEMENT
-- ====================
CREATE TABLE evenement (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nom VARCHAR(150),
                           description TEXT,
                           date_evenement DATE,
                           lieu VARCHAR(255),
                           client_id BIGINT,
                           FOREIGN KEY (client_id) REFERENCES client(id)
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
