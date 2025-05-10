// src/components/DisponibiliteForm.jsx
import React, { useState, useEffect } from 'react';

// Formulaire pour ajouter/modifier une période de disponibilité
const DisponibiliteForm = ({ onSubmit, initialData = null, onCancel, formError = null }) => {
    // initialData pourrait contenir { dateDispo: "YYYY-MM-DD", heureDebut: "HH:MM", heureFin: "HH:MM" }
    const [dateDispo, setDateDispo] = useState('');
    const [heureDebut, setHeureDebut] = useState('');
    const [heureFin, setHeureFin] = useState('');

    const [loading, setLoading] = useState(false);
    const [internalError, setInternalError] = useState(''); // Erreur spécifique au formulaire

    useEffect(() => {
        if (initialData) {
            setDateDispo(initialData.dateDispo || ''); // Format YYYY-MM-DD
            setHeureDebut(initialData.heureDebut || ''); // Format HH:MM
            setHeureFin(initialData.heureFin || '');   // Format HH:MM
        } else {
            // Réinitialiser pour un nouveau formulaire
            setDateDispo('');
            setHeureDebut('');
            setHeureFin('');
        }
    }, [initialData]); // Se met à jour si initialData change (pour l'édition)

    const handleSubmit = async (e) => {
        e.preventDefault();
        setInternalError('');
        setLoading(true);

        if (!dateDispo || !heureDebut || !heureFin) {
            setInternalError("Veuillez remplir tous les champs de date et d'heure.");
            setLoading(false);
            return;
        }

        // TODO: Validation heureFin > heureDebut

        try {
            // La fonction onSubmit est passée en prop depuis le parent (GererDisponibilites)
            await onSubmit({ dateDispo, heureDebut, heureFin });
            // Le parent (GererDisponibilites) gère le message de succès et la réinitialisation
            if (!initialData) { // Réinitialiser seulement si c'est un ajout, pas une édition
                setDateDispo('');
                setHeureDebut('');
                setHeureFin('');
            }
        } catch (err) {
            console.error("Erreur dans DisponibiliteForm onSubmit:", err);
            setInternalError(err.message || "Une erreur est survenue lors de l'enregistrement.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-6 border rounded-lg shadow-md my-6 space-y-4 bg-white max-w-lg mx-auto">
            <h3 className="text-xl font-semibold mb-4 text-gray-800 border-b pb-2">
                {initialData ? 'Modifier' : 'Ajouter'} une Période de Disponibilité
            </h3>

            {(internalError || formError) && (
                <p className="text-red-600 bg-red-100 p-3 rounded text-sm">
                    {internalError || formError}
                </p>
            )}

            <div>
                <label htmlFor="date-dispo" className="block text-sm font-medium text-gray-700 mb-1">Date <span className="text-red-500">*</span></label>
                <input
                    type="date"
                    id="date-dispo"
                    value={dateDispo}
                    onChange={e => setDateDispo(e.target.value)}
                    required
                    disabled={loading}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label htmlFor="heure-debut" className="block text-sm font-medium text-gray-700 mb-1">Heure de Début <span className="text-red-500">*</span></label>
                    <input
                        type="time"
                        id="heure-debut"
                        value={heureDebut}
                        onChange={e => setHeureDebut(e.target.value)}
                        required
                        disabled={loading}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    />
                </div>
                <div>
                    <label htmlFor="heure-fin" className="block text-sm font-medium text-gray-700 mb-1">Heure de Fin <span className="text-red-500">*</span></label>
                    <input
                        type="time"
                        id="heure-fin"
                        value={heureFin}
                        onChange={e => setHeureFin(e.target.value)}
                        required
                        disabled={loading}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    />
                </div>
            </div>

            <div className="flex justify-end pt-4 space-x-3">
                {onCancel && typeof onCancel === 'function' && (
                    <button
                        type="button"
                        onClick={onCancel}
                        disabled={loading}
                        className="py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                        Annuler
                    </button>
                )}
                <button
                    type="submit"
                    disabled={loading}
                    className={`inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
                >
                    {loading ? 'Enregistrement...' : (initialData ? 'Mettre à jour' : 'Ajouter Période')}
                </button>
            </div>
        </form>
    );
};

export default DisponibiliteForm;