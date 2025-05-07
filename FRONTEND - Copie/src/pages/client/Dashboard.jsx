import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCurrentClientInfo, logout } from '../../api/authApi'; // Importez la fonction et logout

function ClientDashboard() {
    const [clientInfo, setClientInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchClientData = async () => {
            setLoading(true);
            setError('');
            try {
                const data = await getCurrentClientInfo();
                setClientInfo(data);
            } catch (err) {
                console.error("Erreur chargement dashboard:", err);
                setError("Impossible de charger vos informations.");
                // Si l'erreur est due à une non-autorisation (token invalide/expiré),
                // getCurrentClientInfo devrait déjà avoir géré la déconnexion et redirection.
                // Sinon, on pourrait forcer la redirection ici aussi.
                // if (err.response?.status === 401 || err.response?.status === 403) {
                //     navigate('/login');
                // }
            } finally {
                setLoading(false);
            }
        };

        fetchClientData();
    }, [navigate]); // Ajouter navigate aux dépendances si utilisé dans le catch

    const handleLogout = () => {
        logout(); // Appelle la fonction de déconnexion de authApi.js
        navigate('/login'); // Rediriger vers la page de connexion
    };

    if (loading) {
        return <div>Chargement du tableau de bord...</div>;
    }

    if (error) {
        return <div><p style={{ color: 'red' }}>{error}</p> <a href="/login">Retourner à la connexion</a></div>;
    }

    return (
        <div>
            <h1>Tableau de Bord Client</h1>
            {clientInfo ? (
                <div>
                    <p>Bienvenue, {clientInfo.prenom} {clientInfo.nom}!</p>
                    <p>Email: {clientInfo.email}</p>
                    <p>Inscrit depuis le: {new Date(clientInfo.dateInscription).toLocaleDateString()}</p>
                    {/* Ajoutez ici d'autres sections du dashboard: Mes réservations, Mes invitations, etc. */}
                </div>
            ) : (
                <p>Impossible de charger les informations du client.</p>
            )}
            <button onClick={handleLogout}>Se déconnecter</button>
        </div>
    );
}

export default ClientDashboard;