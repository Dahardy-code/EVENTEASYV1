import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerClient } from '../../api/authApi'; // Importez la fonction register

function Register() {
    const [nom, setNom] = useState('');
    const [prenom, setPrenom] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (password !== confirmPassword) {
            setError("Les mots de passe ne correspondent pas.");
            return;
        }
         if (password.length < 6) {
             setError("Le mot de passe doit contenir au moins 6 caractères.");
             return;
         }

        try {
            const userData = { nom, prenom, email, password };
            const response = await registerClient(userData);
            console.log('Inscription réussie:', response);

             // Après inscription réussie, rediriger vers le dashboard client
             // (puisque le token est déjà stocké par registerClient)
             if (response.role === 'CLIENT') {
                navigate('/client/dashboard');
             } else {
                // Cas improbable si la logique backend change, rediriger vers login par sécurité
                navigate('/login');
             }

        } catch (err) {
            console.error("Erreur lors de l'inscription:", err);
            setError(err.message || "Erreur lors de la création du compte.");
        }
    };

    return (
        <div>
            <h2>Inscription Client</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="nom">Nom:</label>
                    <input type="text" id="nom" value={nom} onChange={(e) => setNom(e.target.value)} required />
                </div>
                <div>
                    <label htmlFor="prenom">Prénom:</label>
                    <input type="text" id="prenom" value={prenom} onChange={(e) => setPrenom(e.target.value)} required />
                </div>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                </div>
                <div>
                    <label htmlFor="password">Mot de passe:</label>
                    <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                 <div>
                    <label htmlFor="confirmPassword">Confirmer le mot de passe:</label>
                    <input type="password" id="confirmPassword" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button type="submit">S'inscrire</button>
            </form>
            <p>Déjà un compte ? <a href="/login">Connectez-vous</a></p>
        </div>
    );
}

export default Register;