// src/pages/prestataire/GererDisponibilites.jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
// Utiliser les fonctions API de prestataireApi.js
import { getMyDisponibilites, addMyDisponibilite, deleteMyDisponibilite /*, updateMyDisponibilite */ } from '../../api/prestataireApi';
import DisponibiliteForm from '../../components/DisponibiliteForm';

const DisponibiliteItem = ({ dispo, onDelete /*, onEdit */ }) => ( // onEdit est commenté car non implémenté
    <li className="flex justify-between items-center p-3 bg-gray-50 rounded border mb-2 hover:bg-gray-100">
        <div>
            <span className="font-medium text-gray-700">
                {new Date(dispo.dateDispo).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' })} : {dispo.heureDebut} - {dispo.heureFin}
            </span>
        </div>
        <div className="space-x-2">
            {/* <button onClick={() => onEdit(dispo)} className="text-xs text-blue-500 hover:text-blue-700 font-medium">Modifier</button> */}
            <button onClick={() => onDelete(dispo.id)} className="text-xs text-red-500 hover:text-red-700 font-medium">Supprimer</button>
        </div>
    </li>
);

const GererDisponibilites = () => {
    const [disponibilites, setDisponibilites] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [formError, setFormError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingDispo, setEditingDispo] = useState(null);
    const navigate = useNavigate();

    const fetchDisponibilites = async () => {
        setLoading(true); setError('');
        try {
            const data = await getMyDisponibilites();
            setDisponibilites(data || []);
        } catch (err) {
            setError("Impossible de charger vos disponibilités.");
            if (err.response?.status === 401 || err.response?.status === 403) navigate('/login');
        } finally { setLoading(false); }
    };

    useEffect(() => { fetchDisponibilites(); }, []);

    const handleFormSubmit = async (formData) => {
        setFormError('');
        try {
            setLoading(true); // Indiquer le chargement
            if (editingDispo) {
                // await updateMyDisponibilite(editingDispo.id, formData); // TODO: Implémenter dans prestataireApi.js
                alert("Disponibilité mise à jour (simulation)!");
                console.warn("Mise à jour de disponibilité non entièrement implémentée.");
            } else {
                await addMyDisponibilite(formData);
                alert("Disponibilité ajoutée avec succès !");
            }
            setShowForm(false); setEditingDispo(null);
            fetchDisponibilites();
        } catch (err) {
            setFormError(err.response?.data?.message || err.message || "Erreur lors de l'enregistrement.");
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteDisponibilite = async (dispoId) => {
        if (window.confirm("Êtes-vous sûr de vouloir supprimer cette période ?")) {
            setError('');
            try {
                setLoading(true);
                await deleteMyDisponibilite(dispoId);
                setDisponibilites(prev => prev.filter(d => d.id !== dispoId));
                alert("Disponibilité supprimée.");
            } catch (err) {
                setError(err.response?.data?.message || err.message || "Erreur lors de la suppression.");
            } finally {
                setLoading(false);
            }
        }
    };

    const handleEditDisponibilite = (dispo) => {
        setEditingDispo(dispo);
        setShowForm(true);
        setFormError('');
    };
    const handleCancelForm = () => { setShowForm(false); setEditingDispo(null); setFormError(''); };

    if (loading && disponibilites.length === 0) return <div className="text-center py-10 text-gray-600">Chargement...</div>;

    return (
        <div className="container mx-auto px-4 py-8">
            <div className="flex justify-between items-center mb-6 pb-4 border-b">
                <h1 className="text-3xl font-semibold text-gray-800">Mes Disponibilités</h1>
                <button onClick={() => { handleEditDisponibilite(null); }} className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-md shadow">
                    {showForm && !editingDispo ? "Ajout..." : "Ajouter Période"}
                </button>
            </div>

            {error && !showForm && <div className="mb-4 text-center p-3 text-red-700 bg-red-100 rounded border border-red-300">{error}</div>}
            {showForm && <DisponibiliteForm onSubmit={handleFormSubmit} initialData={editingDispo} onCancel={handleCancelForm} formError={formError} />}

            <div className="mt-8">
                <h2 className="text-xl font-semibold text-gray-700 mb-3">Périodes Enregistrées</h2>
                {!loading && disponibilites.length === 0 && !error && (
                    <p className="text-gray-500 italic text-center py-5">Aucune période de disponibilité.</p>
                )}
                {disponibilites.length > 0 && (
                    <ul className="bg-white shadow rounded-lg p-4 border">
                        {disponibilites.map(dispo => (
                            <DisponibiliteItem key={dispo.id} dispo={dispo} onDelete={handleDeleteDisponibilite} onEdit={handleEditDisponibilite} />
                        ))}
                    </ul>
                )}
            </div>
            <div className="mt-10"> <Link to="/prestataire/dashboard" className="text-indigo-600 hover:text-indigo-800 font-medium"> ← Retour </Link> </div>
        </div>
    );
};
export default GererDisponibilites;