import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getReservations } from '../../api/serviceApi';

const Reservations = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const data = await getReservations();
        if (data) {
          setReservations(data);
        } else {
          setError('Aucune réservation trouvée');
        }
      } catch (err) {
        setError('Erreur lors du chargement des réservations');
      } finally {
        setLoading(false);
      }
    };
    fetchReservations();
  }, []);

  if (loading) {
    return <p>Chargement des réservations...</p>;
  }

  if (error) {
    return <p style={{ color: 'red' }}>{error}</p>;
  }

  return (
    <div>
      <h2>Mes réservations</h2>
      <ul>
        {reservations.map(reservation => (
          <li key={reservation.id}>
            <p>
              {reservation.eventName} - {reservation.date}
            </p>
            <button
              onClick={() => navigate(`/client/reservation/${reservation.id}`)}
            >
              Voir la réservation
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Reservations;
