-- data_eventeasy.sql

-- ====================
-- INSERT UTILISATEUR (CORRIGÉ - SANS COLONNE 'role')
-- ====================
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe) VALUES
                                                               ('El Mardy', 'Taha', 'taha@mail.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'), -- Remplacez par un VRAI HASH Bcrypt
                                                               ('Youssef', 'El Aouadi', 'youssef@mail.com', '$2a$10$yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy'), -- Remplacez par un VRAI HASH Bcrypt
                                                               ('Admin', 'Super', 'admin@mail.com', '$2a$10$zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz'); -- Remplacez par un VRAI HASH Bcrypt

-- !!! IMPORTANT !!!
-- Les mots de passe 'hashedpassword123', 'hashedpassword456', 'adminpass789'
-- DOIVENT être remplacés par de VRAIS hashs Bcrypt générés par votre application
-- ou un outil externe. Vous ne pouvez PAS insérer des mots de passe en clair
-- si votre UserDetailsServiceImpl les compare avec passwordEncoder.matches().
-- Exemple de Hash Bcrypt (pour 'password'): $2a$10$dXJ3SWb..qUOu0fJ0iQcLO/j/T4.0..lRnsU7W7W.4Z4.xU4f.q

-- ====================
-- INSERT CLIENT
-- ID correspond à l'ID généré dans la table utilisateur pour 'taha@mail.com' (supposons 1)
-- ====================
INSERT INTO client (id, date_inscription) VALUES
    (1, '2024-04-01');

-- ====================
-- INSERT PRESTATAIRE
-- ID correspond à l'ID généré dans la table utilisateur pour 'youssef@mail.com' (supposons 2)
-- ====================
INSERT INTO prestataire (id, nom_entreprise, categorie_service, adresse, numero_tel) VALUES
    (2, 'Prestataire Pro', 'Traiteur', 'Rabat, Maroc', '+212600000000');

-- ====================
-- INSERT ADMINISTRATEUR
-- ID correspond à l'ID généré dans la table utilisateur pour 'admin@mail.com' (supposons 3)
-- ====================
INSERT INTO administrateur (id, privileges) VALUES
    (3, 'ALL_PRIVILEGES');

-- ====================
-- INSERT SERVICE
-- prestataire_id fait référence à l'ID du prestataire (supposons 2)
-- ====================
INSERT INTO service (titre, description, prix, categorie, prestataire_id) VALUES
                                                                              ('Buffet Mariage', 'Buffet complet pour 200 personnes.', 15000.00, 'Traiteur', 2),
                                                                              ('Location Salle', 'Salle de mariage luxueuse.', 8000.00, 'Salle', 2);

-- ====================
-- INSERT EVENEMENT
-- client_id fait référence à l'ID du client (supposons 1)
-- ====================
INSERT INTO evenement (nom, description, date_evenement, lieu, client_id) VALUES
    ('Mariage de Taha', 'Mariage organisé à Rabat.', '2025-05-01', 'Rabat', 1);

-- ====================
-- INSERT RESERVATION
-- client_id = 1, service_id fait référence aux IDs générés pour service (supposons 1 et 2)
-- ====================
INSERT INTO reservation (date_reservation, statut, client_id, service_id) VALUES
                                                                              ('2025-05-01 14:00:00', 'CONFIRMEE', 1, 1),
                                                                              ('2025-06-10 18:30:00', 'EN_ATTENTE', 1, 2);

-- ====================
-- INSERT PAIEMENT
-- reservation_id fait référence aux IDs générés pour reservation (supposons 1 et 2)
-- ====================
INSERT INTO paiement (montant, date_paiement, mode_paiement, reservation_id) VALUES
                                                                                 (15000.00, '2025-05-02 10:00:00', 'Carte Bancaire', 1),
                                                                                 (8000.00, '2025-06-11 09:30:00', 'Espèces', 2);

-- ====================
-- INSERT AVIS
-- client_id = 1, service_id = 1 et 2
-- ====================
INSERT INTO avis (commentaire, note, date_avis, client_id, service_id) VALUES
                                                                           ('Excellent service !', 5, '2025-05-03 16:00:00', 1, 1),
                                                                           ('Très belle salle.', 4, '2025-06-12 12:00:00', 1, 2);

-- ====================
-- INSERT NOTIFICATION
-- utilisateur_id = 1
-- ====================
INSERT INTO notification (message, date_envoi, utilisateur_id) VALUES
                                                                   ('Votre réservation a été confirmée.', '2025-05-01 15:00:00', 1),
                                                                   ('Nouveau service disponible.', '2025-04-20 10:30:00', 1);

-- ====================
-- INSERT PROMO
-- ====================
INSERT INTO promo (code_promo, description, pourcentage_reduction, date_debut, date_fin) VALUES
    ('MARIAGE2025', 'Promo spéciale mariage', 10.00, '2025-04-01', '2025-12-31');

-- ====================
-- INSERT DISPONIBILITE
-- prestataire_id = 2
-- ====================
INSERT INTO disponibilite (date_dispo, heure_debut, heure_fin, prestataire_id) VALUES
                                                                                   ('2025-05-01', '09:00:00', '23:00:00', 2),
                                                                                   ('2025-05-02', '09:00:00', '23:00:00', 2);

-- ====================
-- INSERT INVITATION
-- client_id = 1, evenement_id fait référence à l'ID généré pour evenement (supposons 1)
-- ====================
INSERT INTO invitation (email, message, date_envoi, client_id, evenement_id) VALUES
                                                                                 ('invite1@mail.com', 'Vous êtes invité à notre mariage.', '2025-04-20 18:00:00', 1, 1),
                                                                                 ('invite2@mail.com', 'Nous serions honorés de votre présence.', '2025-04-21 12:00:00', 1, 1);

-- ====================
-- INSERT STATISTIQUE
-- ====================
INSERT INTO statistique (type_statistique, valeur, date_statistique) VALUES
                                                                         ('Reservations_Mai', 2, '2025-05-31'),
                                                                         ('Avis_positifs', 5, '2025-05-31');