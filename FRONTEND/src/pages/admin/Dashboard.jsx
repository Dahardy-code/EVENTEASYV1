import React, { useEffect, useState } from 'react';
import { getAdminStats } from '../../api/statistiqueApi';  // Assure-toi que cette API est correcte

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);  // Gérer l'état de chargement
  const [error, setError] = useState(null);      // Gérer les erreurs

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await getAdminStats();
        if (data) {
          setStats(data);
        } else {
          setError('Aucune donnée disponible');
        }
      } catch (err) {
        setError('Erreur lors du chargement des statistiques');
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  return (
    <div>
      <h2>Dashboard Admin</h2>
      {loading && <p>Chargement des statistiques...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {stats && !loading && !error && (
        <div>
          <p>Nombre total de réservations : {stats.totalReservations}</p>
          <p>Services les plus réservés : {stats.popularServices}</p>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
