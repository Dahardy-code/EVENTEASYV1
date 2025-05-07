// src/components/InvitationForm.jsx
import React, { useState } from 'react';

// Placeholder form for sending event invitations
const InvitationForm = ({ onSubmit, eventTitle = 'votre événement' }) => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState(`Bonjour,\n\nJe vous invite à ${eventTitle}.\n\nCordialement,`);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);
    console.log('Sending invitation:', { email, message });
    try {
        // if (onSubmit) {
        //   await onSubmit({ email, message }); // Pass data to parent handler
        // }
       // Simulate API call
       await new Promise(resolve => setTimeout(resolve, 1200));
       console.log("Invitation envoyée (simulation).");
       setSuccess(`Invitation envoyée avec succès à ${email}`);
       setEmail(''); // Optionally clear form on success
       //setMessage('');
    } catch (err) {
       console.error("Erreur envoi invitation:", err);
       setError(err.message || "Impossible d'envoyer l'invitation.");
    } finally {
       setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-6 border rounded-lg shadow-md my-4 space-y-4 bg-white max-w-lg mx-auto">
      <h3 className="text-xl font-semibold mb-4 text-gray-800 border-b pb-2">Envoyer une Invitation</h3>

      {error && <p className="text-red-600 bg-red-100 p-3 rounded text-sm">{error}</p>}
      {success && <p className="text-green-700 bg-green-100 p-3 rounded text-sm">{success}</p>}

       <div>
            <label htmlFor="invite-email" className="block text-sm font-medium text-gray-700 mb-1">Email du destinataire:</label>
            <input
                type="email" id="invite-email" value={email} required
                onChange={e => setEmail(e.target.value)}
                disabled={loading}
                placeholder="exemple@domaine.com"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
            />
        </div>
        <div>
            <label htmlFor="invite-message" className="block text-sm font-medium text-gray-700 mb-1">Message:</label>
            <textarea
                id="invite-message" value={message}
                onChange={e => setMessage(e.target.value)}
                rows="4"
                disabled={loading}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
            ></textarea>
        </div>

      <div className="flex justify-end pt-2">
          <button
              type="submit"
              disabled={loading}
              className={`inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
          >
             {loading ? 'Envoi...' : 'Envoyer Invitation'}
          </button>
      </div>
    </form>
  );
};

export default InvitationForm;