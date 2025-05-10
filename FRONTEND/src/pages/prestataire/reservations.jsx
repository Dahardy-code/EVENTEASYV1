import React, { useState, useEffect } from 'react';

const Reservations = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Simuler une requête API pour récupérer les réservations
    const fetchReservations = async () => {
      try {
        const response = await fetch('/api/reservations'); // Remplacez par votre endpoint API
        const data = await response.json();
        setReservations(data);
      } catch (err) {
        setError('Erreur lors du chargement des réservations.');
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, []);

  if (loading) {
    return <div className="text-center text-lg font-semibold">Chargement des réservations...</div>;
  }

  if (error) {
    return <div className="text-center text-red-500">{error}</div>;
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Mes Réservations</h1>
      {reservations.length === 0 ? (
        <div className="text-center text-gray-600">Aucune réservation trouvée.</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {reservations.map((reservation) => (
            <div
              key={reservation.id}
              className="bg-white shadow-md rounded-lg p-4 border border-gray-200"
            >
              <h2 className="text-xl font-semibold text-gray-800">{reservation.eventName}</h2>
              <p className="text-gray-600">Date : {reservation.date}</p>
              <p className="text-gray-600">Client : {reservation.clientName}</p>
              <p className="text-gray-600">Statut : {reservation.status}</p>
              <button className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                Voir les détails
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Reservations;