// src/pages/prestataire/MesEvenements.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getMyServiceReservations } from '../../api/prestataireApi'; // Réutiliser pour voir les événements auxquels il participe

const MesEvenements = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMyLinkedEvents = async () => {
      setLoading(true); setError('');
      try {
        // Les "événements" d'un prestataire sont les réservations de ses services
        const data = await getMyServiceReservations();
        setReservations(data || []);
      } catch (err) {
        setError("Impossible de charger les événements liés à vos services.");
        if (err.response?.status === 401 || err.response?.status === 403) navigate('/login');
      } finally { setLoading(false); }
    };
    fetchMyLinkedEvents();
  }, [navigate]);


  if (loading) return <div className="text-center p-10">Chargement de vos événements...</div>;
  if (error) return <div className="text-center p-10 text-red-500 bg-red-100 rounded">{error}</div>;

  return (
    <div className="container mx-auto p-4 md:p-8">
        <div className="flex justify-between items-center mb-6 pb-4 border-b">
            <h1 className="text-3xl font-semibold text-gray-800">Événements Impliquant Mes Services</h1>
        </div>

        {reservations.length === 0 ? (
             <p className="text-gray-500 italic">Aucun événement n'implique actuellement vos services via des réservations.</p>
        ) : (
            <div className="space-y-4">
                {reservations.map(resa => (
                    <div key={resa.id} className="bg-white p-4 rounded-lg shadow border">
                        {/* TODO: Le backend devrait enrichir ces données avec le nom de l'événement si possible */}
                        <h2 className="text-lg font-semibold text-indigo-600">Service: {resa.serviceTitre || 'Service Indéfini'}</h2>
                        <p className="text-sm text-gray-700">Client: {resa.clientNom || 'Client Anonyme'}</p>
                        <p className="text-sm text-gray-500">Date de l'événement/réservation: {new Date(resa.dateReservation).toLocaleString('fr-FR')}</p>
                        <p className="text-sm">Statut: <span className={`font-medium ${resa.statut === 'CONFIRMEE' ? 'text-green-600' : 'text-yellow-600'}`}>{resa.statut}</span></p>
                        {/* Lien vers la page de détail de la réservation si elle existe */}
                         {/* <Link to={`/prestataire/reservations/${resa.id}`}>Voir détails</Link> */}
                    </div>
                ))}
            </div>
        )}
         <div className="mt-10">
            <Link to="/prestataire/dashboard" className="text-indigo-600 hover:text-indigo-800 font-medium">
                ← Retour au Tableau de Bord
            </Link>
        </div>
    </div>
  );
};

export default MesEvenements;