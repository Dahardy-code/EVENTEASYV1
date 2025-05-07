// src/pages/auth/Register.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerClient, registerPrestataire } from '../../api/authApi';

function Register() {
    const [nom, setNom] = useState('');
    const [prenom, setPrenom] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('CLIENT');
    const [nomEntreprise, setNomEntreprise] = useState('');
    const [categorieService, setCategorieService] = useState('');
    const [adresse, setAdresse] = useState('');
    const [numeroTel, setNumeroTel] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        if (password !== confirmPassword) { setError("Les mots de passe ne correspondent pas."); setLoading(false); return; }
        if (password.length < 6) { setError("Le mot de passe doit contenir au moins 6 caractères."); setLoading(false); return; }

        let baseUserData = { nom, prenom, email, password };
        let finalUserData;
        let registrationFunction;

        if (role === 'CLIENT') {
            finalUserData = { ...baseUserData, role: 'CLIENT' };
            registrationFunction = registerClient;
            console.log("Préparation inscription CLIENT:", finalUserData);
        } else if (role === 'PRESTATAIRE') {
            if (!nomEntreprise || !categorieService) { setError("Le nom de l'entreprise et la catégorie sont requis."); setLoading(false); return; }
            finalUserData = { ...baseUserData, role: 'PRESTATAIRE', nomEntreprise, categorieService, adresse, numeroTel };
            registrationFunction = registerPrestataire;
            console.log("Préparation inscription PRESTATAIRE:", finalUserData);
        } else { setError("Rôle invalide."); setLoading(false); return; }

        try {
            if (typeof registrationFunction !== 'function') {
                setError(`Inscription pour ${role} non disponible.`); setLoading(false); return;
            }

            const response = await registrationFunction(finalUserData);
            console.log('Inscription réussie, réponse:', response);
            setLoading(false);

            // --- CORRECTION : Redirection basée sur le rôle après succès ---
            if (response && response.role) {
                 console.log(`Register handleSubmit: Redirection vers dashboard pour rôle : ${response.role}`);
                if (response.role === 'CLIENT') {
                    // Option A: Vers dashboard client
                     navigate('/client/dashboard');
                    // Option B: Vers page d'accueil
                    // navigate('/');
                } else if (response.role === 'PRESTATAIRE') {
                    // *** Rediriger DIRECTEMENT vers le dashboard prestataire ***
                    navigate('/prestataire/dashboard');
                } else if (response.role === 'ADMIN') { // Au cas où un admin pourrait s'inscrire ainsi
                     navigate('/admin/dashboard');
                } else {
                    console.warn("Register handleSubmit: Rôle non reconnu reçu:", response.role, "Redirection vers /");
                    navigate('/'); // Fallback
                }
            } else {
                 console.error("Register handleSubmit: Réponse d'inscription invalide ou rôle manquant:", response);
                 setError("Erreur lors de la récupération des informations après inscription.");
                 // Rester sur la page pour afficher l'erreur
            }
            // --- FIN CORRECTION ---

        } catch (err) {
            console.error("Erreur détaillée lors de l'inscription:", err);
            setLoading(false);
            const backendError = typeof err === 'string' ? err : (err.message || err.error || "Une erreur interne est survenue lors de l'inscription.");
            setError(backendError);
        }
    };

    // --- JSX (Identique) ---
    return (
         <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-100 via-blue-100 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
                {/* Titre et lien vers Login */}
                <div> <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">Créez votre compte Eventeasy</h2> <p className="mt-2 text-center text-sm text-gray-600"> Ou{' '} <Link to="/login" className="font-medium text-indigo-600 hover:text-indigo-500"> connectez-vous si vous avez déjà un compte </Link> </p> </div>
                {/* Formulaire */}
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    {/* Sélection Rôle */}
                    <fieldset className="mt-4"> <legend className="block text-sm font-medium text-gray-700 mb-2">Je suis un :</legend> <div className="flex items-center space-x-4"> <div className="flex items-center"> <input id="roleClient" name="role" type="radio" value="CLIENT" checked={role === 'CLIENT'} onChange={(e) => setRole(e.target.value)} className="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300" disabled={loading} /> <label htmlFor="roleClient" className="ml-2 block text-sm text-gray-900">Client</label> </div> <div className="flex items-center"> <input id="rolePrestataire" name="role" type="radio" value="PRESTATAIRE" checked={role === 'PRESTATAIRE'} onChange={(e) => setRole(e.target.value)} className="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300" disabled={loading} /> <label htmlFor="rolePrestataire" className="ml-2 block text-sm text-gray-900">Prestataire</label> </div> </div> </fieldset>
                    {/* Champs communs */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4"> <div> <label htmlFor="nom" className="block text-sm font-medium text-gray-700">{role === 'PRESTATAIRE' ? 'Nom (Contact)' : 'Nom:'}</label> <input type="text" id="nom" value={nom} onChange={(e) => setNom(e.target.value)} required className="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> <div> <label htmlFor="prenom" className="block text-sm font-medium text-gray-700">{role === 'PRESTATAIRE' ? 'Prénom (Contact)' : 'Prénom:'}</label> <input type="text" id="prenom" value={prenom} onChange={(e) => setPrenom(e.target.value)} required className="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> </div>
                    <div> <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email:</label> <input type="email" id="email" value={email} autoComplete="email" onChange={(e) => setEmail(e.target.value)} required className="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div>
                    <div> <label htmlFor="password" className="block text-sm font-medium text-gray-700">Mot de passe:</label> <input type="password" id="password" value={password} autoComplete="new-password" onChange={(e) => setPassword(e.target.value)} required className="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div>
                    <div> <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">Confirmer le mot de passe:</label> <input type="password" id="confirmPassword" value={confirmPassword} autoComplete="new-password" onChange={(e) => setConfirmPassword(e.target.value)} required className="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div>
                    {/* Champs Prestataire Conditionnels */}
                    {role === 'PRESTATAIRE' && ( <> <hr className="my-4 border-gray-300"/> <h3 className="text-lg font-medium text-gray-900 text-center mb-4">Informations Prestataire</h3> <div> <label htmlFor="nomEntreprise" className="block text-sm font-medium text-gray-700">Nom de l'entreprise <span className="text-red-500">*</span></label> <input type="text" id="nomEntreprise" value={nomEntreprise} onChange={(e) => setNomEntreprise(e.target.value)} required className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> <div> <label htmlFor="categorieService" className="block text-sm font-medium text-gray-700">Catégorie principale <span className="text-red-500">*</span></label> <input type="text" id="categorieService" value={categorieService} placeholder="Ex: Traiteur, Photographe, DJ..." onChange={(e) => setCategorieService(e.target.value)} required className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> <div> <label htmlFor="adresse" className="block text-sm font-medium text-gray-700">Adresse (optionnel)</label> <input type="text" id="adresse" value={adresse} onChange={(e) => setAdresse(e.target.value)} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> <div> <label htmlFor="numeroTel" className="block text-sm font-medium text-gray-700">Numéro de téléphone (optionnel)</label> <input type="tel" id="numeroTel" value={numeroTel} onChange={(e) => setNumeroTel(e.target.value)} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} /> </div> </> )}
                    {/* Affichage Erreur */}
                    {error && ( <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mt-4" role="alert"><span className="block sm:inline">{error}</span></div> )}
                    {/* Bouton Submit */}
                    <div className="pt-2"> <button type="submit" disabled={loading} className={`group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`} > {loading && ( <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg> )} {loading ? 'Inscription...' : 'S\'inscrire'} </button> </div>
                </form>
            </div>
        </div>
    );
}

export default Register;