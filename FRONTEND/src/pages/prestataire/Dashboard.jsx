import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getReservations, getEvents, getStatistics } from '../../api/serviceApi'; // Utilise tes API pour récupérer les données

const DashboardPrestataire = () => {
  const [reservations, setReservations] = useState([]);
  const [events, setEvents] = useState([]);
  const [statistics, setStatistics] = useState({});

  useEffect(() => {
    // Appel des API pour récupérer les données
    const fetchData = async () => {
      try {
        const reservationsData = await getReservations(); // Exemple d'API pour récupérer les réservations
        const eventsData = await getEvents(); // Exemple d'API pour récupérer les événements
        const statisticsData = await getStatistics(); // Exemple d'API pour récupérer les statistiques

        setReservations(reservationsData);
        setEvents(eventsData);
        setStatistics(statisticsData);
      } catch (error) {
        console.error("Erreur lors du chargement des données", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="dashboard-container">
      <h1>Tableau de bord Prestataire</h1>

      <div className="dashboard-stats">
        <div className="stat-card">
          <h2>Réservations</h2>
          <p>Total des réservations : {reservations.length}</p>
          <Link to="/prestataire/reservations">Voir les réservations</Link>
        </div>

        <div className="stat-card">
          <h2>Événements</h2>
          <p>Total des événements : {events.length}</p>
          <Link to="/prestataire/evenements">Voir les événements</Link>
        </div>

        <div className="stat-card">
          <h2>Statistiques</h2>
          <p>Réservations totales : {statistics.totalReservations}</p>
          <p>Événements terminés : {statistics.completedEvents}</p>
          <Link to="/prestataire/statistiques">Voir les statistiques détaillées</Link>
        </div>
      </div>

      <div className="dashboard-actions">
        <Link to="/prestataire/services" className="action-button">Gérer les services</Link>
        <Link to="/prestataire/disponibilites" className="action-button">Gérer les disponibilités</Link>
      </div>
    </div>
  );
};

export default DashboardPrestataire;
