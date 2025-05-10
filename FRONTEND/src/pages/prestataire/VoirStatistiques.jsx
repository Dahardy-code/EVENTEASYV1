// src/pages/prestataire/VoirStatistiques.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getMyPrestataireStatistics } from '../../api/prestataireApi';
// import ReservationChart from '../../components/ReservationChart'; // Décommentez si vous l'utilisez

const VoirPrestataireStatistiques = () => {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

     useEffect(() => {
        const fetchStats = async () => {
            setLoading(true); setError('');
            try {
                const data = await getMyPrestataireStatistics();
                setStats(data);
            } catch (err) {
                setError("Impossible de charger vos statistiques.");
                 if (err.response?.status === 401 || err.response?.status === 403) navigate('/login');
            } finally { setLoading(false); }
        };
        fetchStats();
    }, [navigate]);

    if (loading) return <div className="text-center p-10">Chargement des statistiques...</div>;
    if (error) return <div className="text-center p-10 text-red-500 bg-red-100 rounded">{error}</div>;

  return (
    <div className="container mx-auto p-4 md:p-8">
        <div className="mb-6 pb-4 border-b">
            <h1 className="text-3xl font-semibold text-gray-800">Mes Statistiques</h1>
        </div>

        {stats ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-lg shadow border text-center">
                    <h2 className="text-lg font-medium text-gray-500">Services Proposés</h2>
                    <p className="text-4xl font-bold text-blue-600 mt-1">{stats.totalServices || 0}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow border text-center">
                    <h2 className="text-lg font-medium text-gray-500">Réservations Totales</h2>
                    <p className="text-4xl font-bold text-indigo-600 mt-1">{stats.totalReservations || 0}</p>
                </div>
                 <div className="bg-white p-6 rounded-lg shadow border text-center">
                    <h2 className="text-lg font-medium text-gray-500">Réservations à Venir</h2>
                    <p className="text-4xl font-bold text-green-600 mt-1">{stats.upcomingReservations || 0}</p>
                </div>
                 <div className="bg-white p-6 rounded-lg shadow border text-center">
                    <h2 className="text-lg font-medium text-gray-500">Note Moyenne (Avis)</h2>
                    <p className="text-4xl font-bold text-yellow-500 mt-1">{stats.averageRating?.toFixed(1) || 'N/A'}</p>
                </div>
                {/* Exemple d'utilisation du composant Chart (si vous l'avez) */}
                {/* <div className="md:col-span-2 lg:col-span-3">
                     <ReservationChart chartData={stats.reservationsByMonth} title="Réservations par Mois"/>
                </div> */}
            </div>
        ) : (
            !loading && <p className="text-gray-500 italic text-center py-5">Aucune statistique à afficher pour le moment.</p>
        )}
        <div className="mt-10">
            <Link to="/prestataire/dashboard" className="text-indigo-600 hover:text-indigo-800 font-medium">
                ← Retour au Tableau de Bord
            </Link>
        </div>
    </div>
  );
};

export default VoirPrestataireStatistiques;