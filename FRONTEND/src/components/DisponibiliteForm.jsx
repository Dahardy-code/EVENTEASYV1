// src/components/DisponibiliteForm.jsx
import React, { useState } from 'react';

// Placeholder form for managing availability (likely for Prestataires)
const DisponibiliteForm = ({ onSubmit, initialData = null }) => {
  // Example state - replace with actual logic and data structure needed
  const [startDate, setStartDate] = useState(initialData?.startDate || '');
  const [endDate, setEndDate] = useState(initialData?.endDate || '');
  const [isAvailable, setIsAvailable] = useState(initialData?.isAvailable ?? true);
  const [notes, setNotes] = useState(initialData?.notes || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    console.log('Submitting availability:', { startDate, endDate, isAvailable, notes });
    try {
       // if (onSubmit) {
       //   await onSubmit({ startDate, endDate, isAvailable, notes });
       // }
       // Simulate API call
       await new Promise(resolve => setTimeout(resolve, 1000));
       console.log("Disponibilité enregistrée (simulation).");
       // Handle success (e.g., close modal, show message)
    } catch (err) {
       console.error("Erreur enregistrement disponibilité:", err);
       setError(err.message || "Impossible d'enregistrer la disponibilité.");
    } finally {
       setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-6 border rounded-lg shadow-md my-4 space-y-4 bg-white max-w-lg mx-auto">
      <h3 className="text-xl font-semibold mb-4 text-gray-800 border-b pb-2">
        {initialData ? 'Modifier' : 'Ajouter'} une Période de Disponibilité
      </h3>

      {error && <p className="text-red-600 bg-red-100 p-3 rounded text-sm">{error}</p>}

       {/* TODO: Replace with proper date range picker component if needed */}
       <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
           <div>
                <label htmlFor="start-date" className="block text-sm font-medium text-gray-700 mb-1">Date de Début:</label>
                <input type="date" id="start-date" value={startDate} onChange={e => setStartDate(e.target.value)} required disabled={loading} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"/>
           </div>
           <div>
                <label htmlFor="end-date" className="block text-sm font-medium text-gray-700 mb-1">Date de Fin:</label>
                <input type="date" id="end-date" value={endDate} onChange={e => setEndDate(e.target.value)} required disabled={loading} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"/>
           </div>
       </div>

        <div>
           <label className="block text-sm font-medium text-gray-700 mb-1">Statut:</label>
           <select
               value={isAvailable ? 'available' : 'unavailable'}
               onChange={e => setIsAvailable(e.target.value === 'available')}
               disabled={loading}
               className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm bg-white py-2 px-3"
           >
               <option value="available">Disponible</option>
               <option value="unavailable">Indisponible</option>
           </select>
       </div>

       <div>
            <label htmlFor="notes" className="block text-sm font-medium text-gray-700 mb-1">Notes (optionnel):</label>
            <textarea
                id="notes" value={notes} onChange={e => setNotes(e.target.value)} rows="2" disabled={loading}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                placeholder="Ex: Congés annuels, événement privé..."
            ></textarea>
       </div>

      <div className="flex justify-end pt-4">
          <button
              type="submit"
              disabled={loading}
              className={`inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
          >
            {loading ? 'Enregistrement...' : (initialData ? 'Mettre à jour' : 'Ajouter Période')}
          </button>
          {/* Optional: Add a cancel button */}
          {/* <button type="button" onClick={onCancel} className="ml-3 ...">Annuler</button> */}
      </div>
    </form>
  );
};

export default DisponibiliteForm;